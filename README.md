Proje monorepo olarak düzenlenmiştir. 

Teknolojiler
- Java (21), Spring Boot (3.5.10)
- MongoDB, MongoDB Compass (Veri tabanı görselleştirme aracı)
- Angular (16.2.0), NodeJS (25.4.0)
- Docker (29.0.1)

IDE : Intellij IDEA


Kullanım
----------
- Proje root klasörüne gidip : docker-compose up -d
- MongoDB Compass -> new Connection -> mongodb://localhost:27017/
- Intellij IDEA'da DynamicObjectSystemApplication sınıfını run ya da debug edelim
- cd frontend ile frontend projesine geçelim. Sistemde node kurulu değilse kuralım. Ardından npm install -> npm start
- http://localhost:4200/ önyüz erişimi
- http://localhost:8080/swagger-ui/index.html swagger erişimi

<img width="2196" height="1438" alt="image" src="https://github.com/user-attachments/assets/eb21dfed-cba4-4577-8b4e-26fea2e4c8fd" />

<img width="2688" height="1438" alt="image" src="https://github.com/user-attachments/assets/97131a6e-fdaf-42d6-95c2-7ae9da86d317" />

ADIMLAR
--------
1) Option ekleme ve listeleme işlemleri yapılır. Burada property item name değerine göre gruplama yapılır.
   <img width="2150" height="1444" alt="image" src="https://github.com/user-attachments/assets/cc0d0e12-e6cc-4a04-8683-82da128052e8" />

2) Bir metedatayı name, entityType ve propertyItemList ile tek seferde oluşturmak için;
    POST - http://localhost:8080/api/metadata - http://localhost:8080/swagger-ui/index.html#/meta-data-controller/createMetaData
   Örnek Veri:
   {
    "name": "IHA",
    "entityType": "CGF",
    "propertyItemList": [
      {
        "type": "SliderComponent",
        "id": 999,
        "itemName": "altitude",
        "title": "İrtifa(m)",
        "unit": "m",
        "option": [],
        "min": 0,
        "max": 8000,
        "defaultValue": 100,
        "propertyItemType": "DEFAULT"
      },
      {
        "type": "PositionComponent",
        "id": 1000,
        "itemName": "Location",
        "title": "Konum",
        "unit": "vector",
        "options": [],
        "min": -180,
        "max": 180,
        "defaultValue": "",
        "propertyItemType": "DEFAULT"
      },
      {
        "type": "DirectionComponent",
        "id": 1001,
        "itemName": "yaw",
        "title": "İstikamet Bilgisi",
        "unit": "°",
        "options": [],
        "min": 0,
        "max": 360,
        "defaultValue": "270",
        "propertyItemType": "DEFAULT"
      },
      {
        "type": "DirectionComponent",
        "id": 22,
        "itemName": "controlDrivingYaw",
        "title": "İstikamet Bilgisi",
        "unit": "°",
        "options": [],
        "min": 0,
        "max": 360,
        "defaultValue": "270",
        "propertyItemType": "DEFAULT"
      },
      {
        "type": "SliderComponent",
        "id": 1002,
        "itemName": "speed",
        "title": "Maksimum Hız(km/h)",
        "unit": "km/h",
        "option": [],
        "min": 0,
        "max": 500,
        "defaultValue": 50,
        "propertyItemType": "LEADER_ONLY"
      },
      {
        "type": "SliderComponent",
        "id": 21,
        "itemName": "controlDrivingSpeed",
        "title": "Maksimum Hız(km/h)",
        "unit": "km/h",
        "option": [],
        "min": 0,
        "max": 500,
        "defaultValue": 0,
        "propertyItemType": "DEFAULT"
      },
      {
        "type": "SelectComponent",
        "id": 1011,
        "itemName": "teamType",
        "title": "Tehditin Sınıflandırılması",
        "unit": "option",
        "options": [
          {
            "value": "0",
            "label": "DÜŞMAN"
          },
          {
            "value": "1",
            "label": "DOST"
          }
        ],
        "min": 0,
        "max": 0,
        "defaultValue": "2",
        "propertyItemType": "LEADER_ONLY"
      },
      {
        "type": "HiddenComponent",
        "id": 1012,
        "itemName": "operationCondition",
        "title": "Hasar Durumu",
        "unit": "option",
        "options": [
          {
            "value": "NOT_SPECIFIED",
            "label": "BELİRTİLMEDİ"
          },
          {
            "value": "DAMAGED",
            "label": "HASARLI"
          },
          {
            "value": "DESTROYED",
            "label": "YOK EDİLMİŞ"
          }
        ],
        "min": 0,
        "max": 0,
        "defaultValue": "NOT_SPECIFIED",
        "propertyItemType": "DEFAULT"
      },
      {
        "type": "SwitchComponent",
        "id": 1013,
        "itemName": "activeStatus",
        "title": "Aktif / Pasif",
        "unit": "boolean",
        "options": [
          {
            "value": "true",
            "label": "AKTİF"
          },
          {
            "value": "false",
            "label": "PASİF"
          }
        ],
        "min": 0,
        "max": 0,
        "defaultValue": "true",
        "propertyItemType": "LEADER_AND_MEMBER_ONLY"
      },
      {
        "type": "SwitchComponent",
        "id": 1014,
        "itemName": "freezeStatus",
        "title": "Dondurma",
        "unit": "boolean",
        "options": [
          {
            "value": "true",
            "label": "DONDUR"
          },
          {
            "value": "false",
            "label": "YÜRÜT"
          }
        ],
        "min": 0,
        "max": 0,
        "defaultValue": "false",
        "showContextMenu": true,
        "propertyItemType": "LEADER_ONLY"
      }
    ]
  }
