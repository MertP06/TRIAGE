import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { apiGet, apiPost } from '../api';

const DoctorNotes = () => {
    const { appointmentId } = useParams();
    const navigate = useNavigate();
    const [appointment, setAppointment] = useState(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [markDone, setMarkDone] = useState(true);

    const [form, setForm] = useState({
        diagnosis: '',
        secondaryDiagnosis: '',
        plan: '',
        prescription: '',
        labOrders: '',
        followUpDate: '',
        followUpNotes: '',
        referralNeeded: false,
        referralDepartment: '',
        restDays: ''
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const detail = await apiGet(`/appointments/${appointmentId}/detail`);
                setAppointment(detail);
            } catch (err) {
                console.error('Veriler yüklenemedi:', err);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [appointmentId]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setForm({ ...form, [name]: type === 'checkbox' ? checked : value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!form.diagnosis.trim() || !form.plan.trim()) {
            alert('Tanı ve plan alanları zorunludur');
            return;
        }
        setSubmitting(true);
        try {
            await apiPost(`/doctor-notes?markDone=${markDone}`, {
                appointmentId: parseInt(appointmentId),
                diagnosis: form.diagnosis,
                secondaryDiagnosis: form.secondaryDiagnosis || null,
                plan: form.plan,
                prescription: form.prescription || null,
                labOrders: form.labOrders || null,
                followUpDate: form.followUpDate || null,
                followUpNotes: form.followUpNotes || null,
                referralNeeded: form.referralNeeded,
                referralDepartment: form.referralDepartment || null,
                restDays: form.restDays ? parseInt(form.restDays) : null
            });
            alert('Doktor notu kaydedildi!');
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
                <h1>✏️ Doktor Notu</h1>
                {appointment?.patient && (
                    <div className="patient-banner">
                        <span className="queue">#{appointment.appointment?.queueNumber}</span>
                        <span className="name">{appointment.patient.name}</span>
                        <span className="tc">TC: {appointment.patient.tc}</span>
                    </div>
                )}
            </div>

            {appointment?.triageRecords?.length > 0 && (
                <div className="triage-summary">
                    <h3>📋 Triaj Bilgileri</h3>
                    {appointment.triageRecords.map((tr, i) => (
                        <div key={i} className="triage-info">
                            <span className={`level ${tr.triageLevel?.toLowerCase()}`}>{tr.triageLevel}</span>
                            <span>Semptomlar: {tr.nurseSymptomsCsv}</span>
                            {tr.notes && <span>Not: {tr.notes}</span>}
                        </div>
                    ))}
                </div>
            )}

            <form onSubmit={handleSubmit} className="doctor-form">
                <div className="form-section">
                    <h3>🩺 Tanı</h3>
                    <div className="form-group">
                        <label>Birincil Tanı *</label>
                        <input
                            type="text"
                            name="diagnosis"
                            value={form.diagnosis}
                            onChange={handleChange}
                            placeholder="Ana tanı"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>İkincil Tanı</label>
                        <input
                            type="text"
                            name="secondaryDiagnosis"
                            value={form.secondaryDiagnosis}
                            onChange={handleChange}
                            placeholder="Varsa ikincil tanı"
                        />
                    </div>
                </div>

                <div className="form-section">
                    <h3>📝 Tedavi Planı</h3>
                    <div className="form-group">
                        <label>Plan *</label>
                        <textarea
                            name="plan"
                            value={form.plan}
                            onChange={handleChange}
                            rows="3"
                            placeholder="Tedavi planı"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Reçete</label>
                        <textarea
                            name="prescription"
                            value={form.prescription}
                            onChange={handleChange}
                            rows="2"
                            placeholder="İlaçlar ve dozajları"
                        />
                    </div>
                    <div className="form-group">
                        <label>Laboratuvar İstemleri</label>
                        <textarea
                            name="labOrders"
                            value={form.labOrders}
                            onChange={handleChange}
                            rows="2"
                            placeholder="Tetkik istekleri"
                        />
                    </div>
                </div>

                <div className="form-section">
                    <h3>📅 Takip</h3>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Kontrol Tarihi</label>
                            <input
                                type="date"
                                name="followUpDate"
                                value={form.followUpDate}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="form-group">
                            <label>İstirahat (gün)</label>
                            <input
                                type="number"
                                name="restDays"
                                value={form.restDays}
                                onChange={handleChange}
                                min="0"
                                max="365"
                            />
                        </div>
                    </div>
                    <div className="form-group">
                        <label>Takip Notları</label>
                        <textarea
                            name="followUpNotes"
                            value={form.followUpNotes}
                            onChange={handleChange}
                            rows="2"
                            placeholder="Kontrol için notlar"
                        />
                    </div>
                </div>

                <div className="form-section">
                    <h3>🔄 Sevk</h3>
                    <div className="form-group checkbox-group">
                        <label>
                            <input
                                type="checkbox"
                                name="referralNeeded"
                                checked={form.referralNeeded}
                                onChange={handleChange}
                            />
                            Sevk gerekli
                        </label>
                    </div>
                    {form.referralNeeded && (
                        <div className="form-group">
                            <label>Sevk Edilecek Bölüm</label>
                            <input
                                type="text"
                                name="referralDepartment"
                                value={form.referralDepartment}
                                onChange={handleChange}
                                placeholder="Örn: Kardiyoloji"
                            />
                        </div>
                    )}
                </div>

                <div className="form-section">
                    <div className="form-group checkbox-group">
                        <label>
                            <input
                                type="checkbox"
                                checked={markDone}
                                onChange={(e) => setMarkDone(e.target.checked)}
                            />
                            Muayeneyi tamamla
                        </label>
                    </div>
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

export default DoctorNotes;
