# Voucher Management and Redemption

## A projekt futtatása

1. futtatás Maven segítségével:
```bash
mvn spring-boot:run
```
2. futtatás Intellij IDE-ből:
- Jobb klikk a `VoucherManagementApplication` osztályon
- `Run 'VoucherManagementApplication.main()'` kiválasztása

## API végpontok

1. ``` /api/vouchers ``` - GET - Voucherek listázása
2. ``` /api/vouchers ``` - POST - Új voucher létrehozása
3. ``` /api/redeem/{code} ``` - GET - Voucher beváltása

### Tesztelés Postman segítségével

- Authentikáció fülön kiválasztjuk a `Basic Auth`-ot, és megadjuk a felhasználónevet és a jelszót, pédáúl:
  - username: `admin`
  - password: `admin`

![Auth Example](src\main\resources\static\auth.png)

- Post Requesteknél a `Body` fülön a `raw` opciót választjuk, és JSON formátumban adjuk meg az adatokat. Például:
```json
{"code": "TEST123","redemptionLimit": 5,"expiryDate": "2025-12-31T23:59:59"}
```

![Post Request](src\main\resources\static\post.png)

- Redeem Requestnél az URL végére a kódot kell beírni, például:
```
localhost:8080/api/redeem/SINGLE
```
![Redeem Request](src\main\resources\static\redeem.png)