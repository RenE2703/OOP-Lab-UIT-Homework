// Mongoose connection helper for the Products API
const mongoose = require('mongoose');

const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://127.0.0.1:27017/shop_lab3_db';

async function connectDatabase() {
    await mongoose.connect(MONGODB_URI);
    console.log('MongoDB connected to database:', mongoose.connection.name);
}

module.exports = connectDatabase;
