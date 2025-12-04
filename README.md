# 🏥 Hastane Acil Servis Yönetim Sistemi

Modern ve kullanıcı dostu bir hastane acil servis yönetim sistemi.

## 📋 Özellikler

### Hasta Yönetimi
- TC kimlik no ile hasta kaydı
- Hasta geçmişi görüntüleme
- Randevu oluşturma

### Triaj Sistemi
- AI destekli semptom analizi
- Vital bulgu kaydı
- Triaj seviyesi belirleme (Kırmızı/Sarı/Yeşil)

### Doktor Modülü
- Muayene ve tanı girişi
- Reçete yazma
- Laboratuvar istemi
- Sevk işlemleri

### Bekleme Odası Ekranı
- Gerçek zamanlı sıra takibi
- Çağrılan hasta gösterimi
- Otomatik güncelleme

### Dashboard
- Günlük istatistikler
- Triaj dağılımı
- Ortalama bekleme süresi

## 🚀 Kurulum

### Gereksinimler
- Java 17+
- Node.js 18+
- PostgreSQL 14+

### Veritabanı
```sql
CREATE DATABASE hospital_er;
```

### Backend
```bash
cd backend/er-backend
./gradlew bootRun
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

## 🔑 Giriş Bilgileri

| Rol | Kullanıcı | Şifre |
|-----|-----------|-------|
| Hemşire | nurse | nurse123 |
| Doktor | doctor | doctor123 |

## 📡 API Endpoints

### Hastalar
- `GET /api/patients` - Tüm hastalar
- `POST /api/patients` - Hasta oluştur
- `GET /api/patients/{tc}` - Hasta detayı

### Randevular
- `GET /api/appointments` - Günün randevuları
- `POST /api/appointments` - Randevu oluştur
- `PATCH /api/appointments/{id}/status` - Durum güncelle
- `GET /api/appointments/history/{tc}` - Hasta geçmişi
- `GET /api/appointments/dashboard` - İstatistikler
- `GET /api/appointments/waiting-room` - Bekleme ekranı

### Triaj
- `POST /api/triage` - Triaj kaydı oluştur
- `GET /api/triage/by-appointment/{id}` - Randevuya göre triajlar

### Doktor Notları
- `POST /api/doctor-notes` - Not oluştur
- `GET /api/doctor-notes/by-appointment/{id}` - Randevuya göre notlar

## 🛠 Teknolojiler

**Backend:**
- Spring Boot 3.2
- Spring Security
- Spring Data JPA
- PostgreSQL

**Frontend:**
- React 18
- React Router
- Vite

## 📱 Mobil API

Hastalar için mobil uygulama endpointleri:
- `GET /api/appointments/mobile/queue/{tc}` - Sıra durumu

## 📄 Lisans

MIT License
