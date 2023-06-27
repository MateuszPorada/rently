db = db.getSiblingDB('rently_db');
db.createUser(
  {
    user: 'rently_user',
    pwd: 'rently_123',
    roles: [{ role: 'readWrite', db: 'rently_db' }],
  },
);
db.createCollection("messages", { capped: true, size: 5000000, max: 10000 });
db.createCollection('users');