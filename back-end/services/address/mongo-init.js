db = db.getSiblingDB('user_db')
db.createUser({
    user: 'admin',
    pwd: 'admin',
    roles: [
        {
            role: 'readWrite',
            db: 'user_db'
        }
    ]
})