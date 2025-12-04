import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiGet, apiPatch } from '../api';
import { useAuth } from '../auth/AuthContext';

const statusLabels = {
    WAITING: 'Bekliyor',
    CALLED: 'Çağrıldı',
    IN_PROGRESS: 'Muayenede',
    DONE: 'Tamamlandı',
    NO_SHOW: 'Gelmedi'
};

const statusColors = {
    WAITING: '#f0ad4e',
    CALLED: '#5bc0de',
    IN_PROGRESS: '#0275d8',
    DONE: '#5cb85c',
    NO_SHOW: '#d9534f'
};

const Appointments = () => {
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('ALL');
    const { user } = useAuth();
    const navigate = useNavigate();

    const fetchAppointments = async () => {
        try {
            const data = await apiGet('/appointments');
            setAppointments(data);
        } catch (err) {
            console.error('Randevular yüklenemedi:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAppointments();
        const interval = setInterval(fetchAppointments, 10000);
        return () => clearInterval(interval);
    }, []);

    const updateStatus = async (id, status) => {
        try {
            await apiPatch(`/appointments/${id}/status`, { status });
            fetchAppointments();
        } catch (err) {
            alert('Durum güncellenemedi: ' + err.message);
        }
    };

    const filtered = filter === 'ALL' 
        ? appointments 
        : appointments.filter(a => a.status === filter);

    if (loading) return <div className="loading">Yükleniyor...</div>;

    return (
        <div className="appointments-page">
            <div className="page-header">
                <h1>📋 Randevular</h1>
                <div className="filter-tabs">
                    {['ALL', 'WAITING', 'CALLED', 'IN_PROGRESS', 'DONE', 'NO_SHOW'].map(s => (
                        <button
                            key={s}
                            className={`filter-tab ${filter === s ? 'active' : ''}`}
                            onClick={() => setFilter(s)}
                        >
                            {s === 'ALL' ? 'Tümü' : statusLabels[s]}
                            <span className="count">
                                {s === 'ALL' 
                                    ? appointments.length 
                                    : appointments.filter(a => a.status === s).length}
                            </span>
                        </button>
                    ))}
                </div>
            </div>

            <div className="appointments-list">
                {filtered.length === 0 ? (
                    <div className="empty-state">
                        <span className="empty-icon">📭</span>
                        <p>Randevu bulunamadı</p>
                    </div>
                ) : (
                    filtered.map(ap => (
                        <div key={ap.id} className="appointment-card">
                            <div className="card-header">
                                <div className="queue-badge">{ap.queueNumber}</div>
                                <div className="patient-info">
                                    <h3>{ap.patient?.name || 'İsimsiz'}</h3>
                                    <span className="tc">TC: {ap.patient?.tc}</span>
                                </div>
                                <span 
                                    className="status-badge"
                                    style={{ backgroundColor: statusColors[ap.status] }}
                                >
                                    {statusLabels[ap.status]}
                                </span>
                            </div>

                            {ap.chiefComplaint && (
                                <div className="complaint">
                                    <strong>Şikayet:</strong> {ap.chiefComplaint}
                                </div>
                            )}

                            <div className="card-actions">
                                {user?.role === 'NURSE' && (
                                    <>
                                        {ap.status === 'WAITING' && (
                                            <button 
                                                className="btn btn-call"
                                                onClick={() => updateStatus(ap.id, 'CALLED')}
                                            >
                                                📢 Çağır
                                            </button>
                                        )}
                                        {(ap.status === 'WAITING' || ap.status === 'CALLED') && (
                                            <button 
                                                className="btn btn-noshow"
                                                onClick={() => updateStatus(ap.id, 'NO_SHOW')}
                                            >
                                                ❌ Gelmedi
                                            </button>
                                        )}
                                        {ap.status === 'CALLED' && (
                                            <button 
                                                className="btn btn-triage"
                                                onClick={() => navigate(`/triage/${ap.id}`)}
                                            >
                                                📝 Triaj Yap
                                            </button>
                                        )}
                                    </>
                                )}

                                {user?.role === 'DOCTOR' && (
                                    <>
                                        {ap.status === 'CALLED' && (
                                            <button 
                                                className="btn btn-start"
                                                onClick={() => updateStatus(ap.id, 'IN_PROGRESS')}
                                            >
                                                🩺 Muayeneye Al
                                            </button>
                                        )}
                                        {ap.status === 'IN_PROGRESS' && (
                                            <button 
                                                className="btn btn-note"
                                                onClick={() => navigate(`/doctor-note/${ap.id}`)}
                                            >
                                                ✏️ Not Ekle
                                            </button>
                                        )}
                                    </>
                                )}
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default Appointments;
