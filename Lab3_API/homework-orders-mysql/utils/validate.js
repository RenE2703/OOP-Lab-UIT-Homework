// Small validation helpers shared by the services
const ApiError = require('./ApiError');

function toPositiveInt(value, fieldName = 'id') {
    const number = Number(value);
    if (!Number.isInteger(number) || number <= 0) {
        throw new ApiError(400, `${fieldName} must be a positive integer`);
    }
    return number;
}

function roundMoney(value) {
    return Math.round((Number(value) + Number.EPSILON) * 100) / 100;
}

module.exports = { toPositiveInt, roundMoney };
