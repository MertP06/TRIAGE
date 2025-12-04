import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [formError, setFormError] = useState('');
    const { login, isLoading, error } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setFormError('');

        if (!username.trim() || !password.trim()) {
            setFormError('Kullanıcı adı ve şifre gereklidir');
            return;
        }

        const success = await login(username.trim(), password);
        if (success) navigate('/');
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <div className="login-header">
                    <div className="login-logo">🏥</div>
                    <h1>Acil Servis</h1>
                    <p>Yönetim sistemine giriş yapın</p>
                </div>

                <form onSubmit={handleSubmit} className="login-form">
                    {(formError || error) && (
                        <div className="error-message">
                            ⚠️ {formError || error}
                        </div>
                    )}

                    <div className="form-group">
                        <label>Kullanıcı Adı</label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            placeholder="Kullanıcı adınız"
                            disabled={isLoading}
                        />
                    </div>

                    <div className="form-group">
                        <label>Şifre</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Şifreniz"
                            disabled={isLoading}
                        />
                    </div>

                    <button type="submit" className="btn-primary" disabled={isLoading}>
                        {isLoading ? 'Giriş yapılıyor...' : 'Giriş Yap'}
                    </button>
                </form>

                <div className="divider"><span>Demo Hesaplar</span></div>

                <div className="demo-accounts">
                    <div className="demo-account">
                        <span className="demo-account-role">👩‍⚕️ Hemşire</span>
                        <span className="demo-account-creds">nurse / nurse123</span>
                    </div>
                    <div className="demo-account">
                        <span className="demo-account-role">👨‍⚕️ Doktor</span>
                        <span className="demo-account-creds">doctor / doctor123</span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
