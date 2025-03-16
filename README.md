# API Test Otomasyonu

Bu proje, Java ve RestAssured kullanarak Restful Booker API'sini test etmek için geliştirilmiştir. Testler, API'nin çeşitli uç endpoint değerlerine yapılan HTTP isteklerini doğrular ve yanıtları değerlendirir.

## Kullanılan Teknolojiler
- **Java 17**
- **RestAssured** (API testleri için)
- **JUnit** (Test frameworkü)
- **AssertJ** (Gelişmiş assertion desteği)
- **Maven** (Bağımlılık yönetimi)

## Proje Yapısı
```
project-root/
│── src/
│   ├── test/
│   │   ├── java/com/zeren/tests/ # API testleri
│   │   ├── java/com/zeren/base/  # API temel işlemleri
│   │   ├── java/com/zeren/utils/ # Yardımcı sınıflar
|   │   ├── resources/
|   │       ├── config.properties  # Konfigürasyon dosyası
│── pom.xml  # Maven bağımlılıkları
│── README.md  # Proje dokümantasyonu
```

## Kurulum ve Çalıştırma
### 1. Projeyi Klonlayın
```sh
git clone https://github.com/MustafaZeren/dias-api-test-automation.git
cd dias-api-test-automation
```

### 2. Bağımlılıkları Yükleyin
```sh
mvn clean install
```

### 3. Testleri Çalıştırın
```sh
mvn test
```

## Testler
Testler, `com.zeren.tests.TestsAPI` sınıfı altında yer almaktadır. Çalıştırılan başlıca testler:

- **Sağlık Kontrolü (Health Check)**: API'nin çalıştığını doğrular.
- **Kimlik Doğrulama (Authentication)**: Kullanıcı adı ve şifre ile token alır.
- **Rezervasyon Listeleme (Get Bookings)**: Mevcut rezervasyonları getirir.
- **Rezervasyon Oluşturma (Create Booking)**: Yeni bir rezervasyon oluşturur.
- **Rezervasyon Güncelleme (Update Booking)**: Var olan rezervasyonu günceller.
- **Rezervasyon Silme (Delete Booking)**: Bir rezervasyonu siler ve tekrar çağrıldığında 404 aldığını doğrular.

## Konfigürasyon
Konfigürasyon dosyası `src/test/resources/config.properties` içinde yer almaktadır:
```properties
base_url=https://restful-booker.herokuapp.com
```

Bu URL, API istekleri için kullanılmaktadır. Gerektiğinde farklı bir URL ile değiştirilebilir.

## Katkıda Bulunma
Bu projeye katkıda bulunmak için:
1. Fork yapın.
2. Yeni bir branch oluşturun: `feature-branch`
3. Değişikliklerinizi yapın ve commit edin.
4. PR (Pull Request) açın.

## Lisans
Bu proje açık kaynak olup, MIT lisansı altında sunulmaktadır.

