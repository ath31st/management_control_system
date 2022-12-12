print("Started Adding the Users.");
db.getCollectionNames().forEach(c=>db[c].drop());
db.createUser(
    {
        user: "mongo",
        pwd: "mongo",
        roles: [
            {
                role: "readWrite",
                db: "mongoDBshop1"
            }
        ]
    }
);
print("End Adding the User Roles.");