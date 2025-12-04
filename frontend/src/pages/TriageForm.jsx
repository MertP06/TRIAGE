import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { apiGet, apiPost } from '../api';

const TriageForm = () => {
    const { appointmentId } = useParams();
    const navigate = useNavigate();
    const [appointment, setAppointment] = useState(null);
    const [symptoms, setSymptoms] = useState([]);
    const [allSymptoms, setAllSymptoms] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);

    const [form, setForm] = useState({
        temperature: '',
        pulse: '',
        bpHigh: '',
        bpLow: '',
        oxygenSaturation: '',
        respiratoryRate: '',
        painLevel: '',
        bloodGlucose: '',
        triageLevel: 'YESIL',
        notes: ''
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [detail, syms] = await Promise.all([
                    apiGet(`/appointments/${appointmentId}/detail`),
                    apiGet('/medical/symptoms')
                ]);
                setAppointment(detail);
                setAllSymptoms(Array.isArray(syms) ? syms : Object.values(syms));
            } catch (err) {
                console.error('Veriler yüklenemedi:', err);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [appointmentId]);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const addSymptom = (symptom) => {
        if (!symptoms.includes(symptom)) {
            setSymptoms([...symptoms, symptom]);
        }
        setSearchTerm('');
    };

    const removeSymptom = (symptom) => {
        setSymptoms(symptoms.filter(s => s !== symptom));
    };

    const filteredSymptoms = allSymptoms
        .filter(s => s.toLowerCase().includes(searchTerm.toLowerCase()))
        .slice(0, 10);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (symptoms.length === 0) {
            alert('En az bir semptom seçmelisiniz');
            return;
        }
        setSubmitting(true);
        try {
            await apiPost('/triage', {
                appointmentId: parseInt(appointmentId),
                nurseSymptomsCsv: symptoms.join(','),
                temperature: form.temperature ? parseFloat(form.temperature) : null,
                pulse: form.pulse ? parseInt(form.pulse) : null,
                bpHigh: form.bpHigh ? parseInt(form.bpHigh) : null,
                bpLow: form.bpLow ? parseInt(form.bpLow) : null,
                oxygenSaturation: form.oxygenSaturation ? parseInt(form.oxygenSaturation) : null,
                respiratoryRate: form.respiratoryRate ? parseInt(form.respiratoryRate) : null,
                painLevel: form.painLevel ? parseInt(form.painLevel) : null,
                bloodGlucose: form.bloodGlucose ? parseInt(form.bloodGlucose) : null,
                triageLevel: form.triageLevel,
                notes: form.notes
            });
            alert('Triaj kaydedildi!');
            navigate('/appointments');
        } catch (err) {
            alert('Hata: ' + err.message);
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <div className="loading">Yükleniyor...</div>;

    return (
        <div className="form-page">
            <div className="form-header">
                <h1>📋 Triaj Formu</h1>
                {appointment?.patient && (
                    <div className="patient-banner">
                        <span className="queue">#{appointment.appointment?.queueNumber}</span>
                        <span className="name">{appointment.patient.name}</span>
                        <span className="tc">TC: {appointment.patient.tc}</span>
                    </div>
                )}
            </div>

            <form onSubmit={handleSubmit} className="triage-form">
                <div className="form-section">
                    <h3>🩺 Vital Bulgular</h3>
                    <div className="vitals-grid">
                        <div className="form-group">
                            <label>Ateş (°C)</label>
                            <input type="number" name="temperature" value={form.temperature}
                                onChange={handleChange} step="0.1" min="30" max="45" placeholder="36.5" />
                        </div>
                        <div className="form-group">
                            <label>Nabız (bpm)</label>
                            <input type="number" name="pulse" value={form.pulse}
                                onChange={handleChange} min="20" max="240" placeholder="80" />
                        </div>
                        <div className="form-group">
                            <label>Tansiyon (Sistolik)</label>
                            <input type="number" name="bpHigh" value={form.bpHigh}
                                onChange={handleChange} min="50" max="260" placeholder="120" />
                        </div>
                        <div className="form-group">
                            <label>Tansiyon (Diastolik)</label>
                            <input type="number" name="bpLow" value={form.bpLow}
                                onChange={handleChange} min="30" max="200" placeholder="80" />
                        </div>
                        <div className="form-group">
                            <label>SpO2 (%)</label>
                            <input type="number" name="oxygenSaturation" value={form.oxygenSaturation}
                                onChange={handleChange} min="50" max="100" placeholder="98" />
                        </div>
                        <div className="form-group">
                            <label>Solunum Hızı</label>
                            <input type="number" name="respiratoryRate" value={form.respiratoryRate}
                                onChange={handleChange} min="5" max="60" placeholder="16" />
                        </div>
                        <div className="form-group">
                            <label>Ağrı (0-10)</label>
                            <input type="number" name="painLevel" value={form.painLevel}
                                onChange={handleChange} min="0" max="10" placeholder="0" />
                        </div>
                        <div className="form-group">
                            <label>Kan Şekeri</label>
                            <input type="number" name="bloodGlucose" value={form.bloodGlucose}
                                onChange={handleChange} min="20" max="600" placeholder="100" />
                        </div>
                    </div>
                </div>

                <div className="form-section">
                    <h3>🔍 Semptomlar</h3>
                    <div className="symptom-search">
                        <input
                            type="text"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            placeholder="Semptom ara..."
                        />
                        {searchTerm && filteredSymptoms.length > 0 && (
                            <div className="symptom-dropdown">
                                {filteredSymptoms.map(s => (
                                    <div key={s} className="symptom-option" onClick={() => addSymptom(s)}>
                                        {s}
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                    <div className="selected-symptoms">
                        {symptoms.map(s => (
                            <span key={s} className="symptom-tag">
                                {s}
                                <button type="button" onClick={() => removeSymptom(s)}>×</button>
                            </span>
                        ))}
                    </div>
                </div>

                <div className="form-section">
                    <h3>🚦 Triaj Seviyesi</h3>
                    <div className="triage-levels">
                        {['KIRMIZI', 'SARI', 'YESIL'].map(level => (
                            <label key={level} className={`level-option ${level.toLowerCase()} ${form.triageLevel === level ? 'selected' : ''}`}>
                                <input
                                    type="radio"
                                    name="triageLevel"
                                    value={level}
                                    checked={form.triageLevel === level}
                                    onChange={handleChange}
                                />
                                <span>{level}</span>
                            </label>
                        ))}
                    </div>
                </div>

                <div className="form-section">
                    <h3>📝 Notlar</h3>
                    <textarea
                        name="notes"
                        value={form.notes}
                        onChange={handleChange}
                        rows="3"
                        placeholder="Ek notlar..."
                    />
                </div>

                <div className="form-actions">
                    <button type="button" onClick={() => navigate('/appointments')} className="btn-cancel">
                        İptal
                    </button>
                    <button type="submit" className="btn-submit" disabled={submitting}>
                        {submitting ? 'Kaydediliyor...' : 'Kaydet'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default TriageForm;
