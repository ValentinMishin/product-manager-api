Описание АПИ:
UI
  http://localhost:8080/swagger-ui/index.html
JSON
  api-docs.json
Для запуска написан конфиг для docker-compose. 

Ответ от внешнего апи(в режиме разработки) в формате:
{
  "id": 1,
  "title": "Название",
  "price": 109.95,
  "description": "Описание",
  "category": "Категория",
  "rating": {
    "rate": 3.9,
    "count": 120
  }
}
В режиме разработки работает по адресу
  http://localhost:8081/api/fproducts
Исходники вспомогательного внешнего сервиса:
  https://github.com/ValentinMishin/fake-store-api 
