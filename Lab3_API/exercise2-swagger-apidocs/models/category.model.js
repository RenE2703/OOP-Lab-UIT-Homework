// Mongoose schema definition for Category (Exercise 1 - extended requirement)
const mongoose = require('mongoose');

// "Home & Living" -> "home-living"
function toSlug(value) {
    return String(value)
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .toLowerCase()
        .replace(/&/g, ' ')
        .replace(/[^a-z0-9]+/g, '-')
        .replace(/^-+|-+$/g, '');
}

const categorySchema = new mongoose.Schema({
    name: {
        type: String,
        required: [true, 'Category name cannot be empty'],
        trim: true,
        unique: true,
        minlength: [3, 'Category name must be at least 3 characters']
    },
    slug: {
        type: String,
        unique: true,
        trim: true
    },
    description: {
        type: String,
        trim: true
    }
}, { timestamps: true });

// Generate the slug automatically whenever the name changes.
categorySchema.pre('save', function generateSlug(next) {
    if (this.isModified('name')) {
        this.slug = toSlug(this.name);
    }
    next();
});

// findByIdAndUpdate/findOneAndUpdate skip document hooks, so regenerate the slug here too.
categorySchema.pre('findOneAndUpdate', function generateSlugOnUpdate(next) {
    const update = this.getUpdate() || {};
    const name = update.name !== undefined ? update.name : update.$set && update.$set.name;

    if (name !== undefined) {
        const slug = toSlug(name);
        if (update.$set) {
            update.$set.slug = slug;
        } else {
            update.slug = slug;
        }
        this.setUpdate(update);
    }
    next();
});

categorySchema.statics.toSlug = toSlug;

module.exports = mongoose.model('Category', categorySchema);
