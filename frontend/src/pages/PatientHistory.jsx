import { useState } from 'react';
import { apiGet } from '../api';

const statusLabels = {
    WAITING: 'Bekliyor',
    CALLED: 'Çağrıldı',
    IN_PROGRESS: 'Muayenede',
    DONE: 'Tamamlandı',
    NO_SHOW: 'Gelmedi'
};

const PatientHistory = () => {
    const [tc, setTc] = useState('');
    const [history, setHistory] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const searchHistory = async () => {
        if (!tc.trim() || tc.length !== 11) {
            setError('Geçerli bir TC kimlik numarası girin (11 haneli)');
            return;
        }
        setLoading(true);
        setError('');
        setHistory(null);
        try {
            const data = await apiGet(`/appointments/history/${tc}`);
            setHistory(data);
        } catch (err) {
            setError('Hasta bulunamadı veya bir hata oluştu');
        } finally {
            setLoading(false);
        }
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') searchHistory();
    };

    const getTriageForAppointment = (appointmentId) => {
        return history?.triageRecords?.filter(t => t.appointment?.id === appointmentId) || [];
    };

    const getNotesForAppointment = (appointmentId) => {
        return history?.doctorNotes?.filter(n => n.appointment?.id === appointmentId) || [];
    };

    return (
        <div className="history-page">
            <div className="page-header">
                <h1>📂 Hasta Geçmişi</h1>
            </div>

            <div className="search-box">
                <input
                    type="text"
                    value={tc}
                    onChange={(e) => setTc(e.target.value.replace(/\D/g, '').slice(0, 11))}
                    onKeyPress={handleKeyPress}
                    placeholder="TC Kimlik No (11 haneli)"
                    maxLength={11}
                />
                <button onClick={searchHistory} disabled={loading}>
                    {loading ? 'Aranıyor...' : '🔍 Ara'}
                </button>
            </div>

            {error && <div className="error-box">{error}</div>}

            {history && (
                <div className="history-content">
                    <div className="patient-card">
                        <h2>{history.patient?.name}</h2>
                        <div className="patient-meta">
                            <span>TC: {history.patient?.tc}</span>
                            {history.patient?.birthYear && (
                                <span>Doğum Yılı: {history.patient.birthYear}</span>
                            )}
                            {history.patient?.gender && (
                                <span>Cinsiyet: {history.patient.gender === 'E' ? 'Erkek' : 'Kadın'}</span>
                            )}
                        </div>
                        <div className="stats-row">
                            <div className="stat">
                                <span className="value">{history.totalAppointments}</span>
                                <span className="label">Randevu</span>
                            </div>
                            <div className="stat">
                                <span className="value">{history.totalTriageRecords}</span>
                                <span className="label">Triaj</span>
                            </div>
                            <div className="stat">
                                <span className="value">{history.totalDoctorNotes}</span>
                                <span className="label">Muayene</span>
                            </div>
                        </div>
                    </div>

                    <div className="timeline">
                        <h3>📅 Randevu Geçmişi</h3>
                        {history.appointments?.length === 0 ? (
                            <p className="no-data">Henüz randevu kaydı yok</p>
                        ) : (
                            history.appointments.map(ap => (
                                <div key={ap.id} className="timeline-item">
                                    <div className="timeline-header">
                                        <span className="date">
                                            {new Date(ap.createdAt).toLocaleDateString('tr-TR')}
                                        </span>
                                        <span className="queue">Sıra: {ap.queueNumber}</span>
                                        <span className={`status ${ap.status.toLowerCase()}`}>
                                            {statusLabels[ap.status]}
                                        </span>
                                    </div>

                                    {ap.chiefComplaint && (
                                        <div className="complaint">
                                            <strong>Şikayet:</strong> {ap.chiefComplaint}
                                        </div>
                                    )}

                                    {getTriageForAppointment(ap.id).map(tr => (
                                        <div key={tr.id} className="triage-record">
                                            <h4>📋 Triaj</h4>
                                            <div className="record-grid">
                                                <span className={`level ${tr.triageLevel?.toLowerCase()}`}>
                                                    {tr.triageLevel}
                                                </span>
                                                {tr.temperature && <span>Ateş: {tr.temperature}°C</span>}
                                                {tr.pulse && <span>Nabız: {tr.pulse}</span>}
                                                {tr.bpHigh && tr.bpLow && (
                                                    <span>Tansiyon: {tr.bpHigh}/{tr.bpLow}</span>
                                                )}
                                                {tr.oxygenSaturation && <span>SpO2: {tr.oxygenSaturation}%</span>}
                                            </div>
                                            {tr.nurseSymptomsCsv && (
                                                <div className="symptoms">
                                                    <strong>Semptomlar:</strong> {tr.nurseSymptomsCsv}
                                                </div>
                                            )}
                                            {tr.notes && <div className="notes">{tr.notes}</div>}
                                        </div>
                                    ))}

                                    {getNotesForAppointment(ap.id).map(note => (
                                        <div key={note.id} className="doctor-record">
                                            <h4>🩺 Doktor Notu</h4>
                                            <div className="diagnosis">
                                                <strong>Tanı:</strong> {note.diagnosis}
                                                {note.secondaryDiagnosis && ` / ${note.secondaryDiagnosis}`}
                                            </div>
                                            <div className="plan">
                                                <strong>Plan:</strong> {note.plan}
                                            </div>
                                            {note.prescription && (
                                                <div className="prescription">
                                                    <strong>Reçete:</strong> {note.prescription}
                                                </div>
                                            )}
                                            {note.followUpDate && (
                                                <div className="followup">
                                                    <strong>Kontrol:</strong> {note.followUpDate}
                                                </div>
                                            )}
                                            {note.restDays && (
                                                <div className="rest">
                                                    <strong>İstirahat:</strong> {note.restDays} gün
                                                </div>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default PatientHistory;

