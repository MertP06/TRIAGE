import { useState, useEffect } from 'react';

const WaitingRoom = () => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [time, setTime] = useState(new Date());

    const fetchData = async () => {
        try {
            const res = await fetch('http://localhost:8080/api/appointments/waiting-room');
            const json = await res.json();
            setData(json);
        } catch (err) {
            console.error('Veri yüklenemedi:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
        const dataInterval = setInterval(fetchData, 5000);
        const timeInterval = setInterval(() => setTime(new Date()), 1000);
        return () => {
            clearInterval(dataInterval);
            clearInterval(timeInterval);
        };
    }, []);

    if (loading) {
        return (
            <div className="waiting-room-loading">
                <div className="spinner"></div>
                <p>Yükleniyor...</p>
            </div>
        );
    }

    return (
        <div className="waiting-room">
            <header className="wr-header">
                <div className="hospital-name">
                    <div className="icon">🏥</div>
                    <h1>Acil Servis Bekleme Ekranı</h1>
                </div>
                <div className="clock">
                    {time.toLocaleTimeString('tr-TR', { hour: '2-digit', minute: '2-digit' })}
                </div>
            </header>

            <main className="wr-content">
                <div className="current-call">
                    {data?.currentCall ? (
                        <>
                            <div className="call-label">Şu An Çağrılan</div>
                            <div className="call-number">{data.currentCall.queueNumber}</div>
                            <div className="call-name">{data.currentCall.patientName}</div>
                            <div className="call-message">{data.currentCall.message}</div>
                        </>
                    ) : (
                        <>
                            <div className="call-label">Bekleniyor</div>
                            <div className="call-number">—</div>
                            <div className="call-message">Şu an çağrılan hasta yok</div>
                        </>
                    )}
                </div>

                <div className="waiting-list">
                    <h2>📋 Bekleyen Sıra Numaraları</h2>
                    <div className="queue-numbers">
                        {data?.waitingList?.length > 0 ? (
                            data.waitingList.map((w, i) => (
                                <div key={i} className="queue-item">
                                    {w.queueNumber}
                                </div>
                            ))
                        ) : (
                            <p className="no-waiting">Bekleyen hasta yok</p>
                        )}
                    </div>
                    <div className="total-waiting">
                        Toplam Bekleyen: <strong>{data?.totalWaiting || 0}</strong>
                    </div>
                </div>
            </main>

            <footer className="wr-footer">
                Son güncelleme: {data?.lastUpdated || '—'}
            </footer>
        </div>
    );
};

export default WaitingRoom;
