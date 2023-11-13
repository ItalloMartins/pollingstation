// init-mongo.js
db = db.getSiblingDB('polling-station');
db.createCollection('pollingStation');

db.createCollection('pauta');
db.createCollection('sessao');
db.createCollection('votos');