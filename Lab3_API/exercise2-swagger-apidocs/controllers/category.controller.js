// Request handlers for the Category API (extended requirement)
const Category = require('../models/category.model');

const ALLOWED_SORT_FIELDS = ['name', 'slug', 'createdAt', 'updatedAt'];

// Friendly 409 before MongoDB raises E11000. The unique index is still the
// last line of defence against two concurrent requests.
async function findDuplicateCategory(name, slug, excludeId) {
    const filter = { $or: [{ name }, { slug }] };
    if (excludeId) filter._id = { $ne: excludeId };
    return Category.findOne(filter).select('_id');
}

function duplicateResponse(res) {
    return res.status(409).json({
        success: false,
        error: 'Category name or slug already exists'
    });
}

// GET /api/categories - pagination + sorting
exports.getAllCategories = async (req, res, next) => {
    try {
        const page = Math.max(Number(req.query.page) || 1, 1);
        const limit = Math.max(Number(req.query.limit) || 5, 1);
        const sortField = ALLOWED_SORT_FIELDS.includes(req.query.sort)
            ? req.query.sort
            : 'createdAt';
        const sortOrder = String(req.query.order).toLowerCase() === 'asc' ? 1 : -1;
        const skip = (page - 1) * limit;

        const totalCategories = await Category.countDocuments();
        const categories = await Category.find()
            .sort({ [sortField]: sortOrder })
            .skip(skip)
            .limit(limit);

        res.status(200).json({
            success: true,
            count: categories.length,
            pagination: {
                currentPage: page,
                totalPages: Math.ceil(totalCategories / limit),
                totalCategories,
                sort: `${sortField}:${sortOrder === 1 ? 'asc' : 'desc'}`
            },
            data: categories
        });
    } catch (error) {
        next(error);
    }
};

// GET /api/categories/:id
exports.getCategoryById = async (req, res, next) => {
    try {
        const category = await Category.findById(req.params.id);
        if (!category) {
            return res.status(404).json({
                success: false,
                message: 'No category found with the given ID'
            });
        }
        res.status(200).json({ success: true, data: category });
    } catch (error) {
        next(error);
    }
};

// POST /api/categories
exports.createCategory = async (req, res, next) => {
    try {
        const name = req.body.name.trim();
        const slug = Category.toSlug(name);

        if (await findDuplicateCategory(name, slug)) {
            return duplicateResponse(res);
        }

        // The pre('save') hook generates the slug from the name.
        const category = await Category.create({
            name,
            description: req.body.description
        });

        res.status(201).json({
            success: true,
            message: 'Category created successfully',
            data: category
        });
    } catch (error) {
        next(error);
    }
};

// PUT /api/categories/:id - full update, regenerates the slug
exports.updateCategory = async (req, res, next) => {
    try {
        const category = await Category.findById(req.params.id);
        if (!category) {
            return res.status(404).json({
                success: false,
                message: 'No category found to update'
            });
        }

        const name = req.body.name.trim();
        if (await findDuplicateCategory(name, Category.toSlug(name), category._id)) {
            return duplicateResponse(res);
        }

        category.name = name;
        category.description = req.body.description;
        await category.save(); // triggers the pre('save') slug hook

        res.status(200).json({
            success: true,
            message: 'Category updated successfully',
            data: category
        });
    } catch (error) {
        next(error);
    }
};

// PATCH /api/categories/:id - partial update
exports.patchCategory = async (req, res, next) => {
    try {
        const category = await Category.findById(req.params.id);
        if (!category) {
            return res.status(404).json({
                success: false,
                message: 'No category found to update'
            });
        }

        if (req.body.name !== undefined) {
            const name = req.body.name.trim();
            if (await findDuplicateCategory(name, Category.toSlug(name), category._id)) {
                return duplicateResponse(res);
            }
            category.name = name;
        }
        if (req.body.description !== undefined) category.description = req.body.description;
        await category.save();

        res.status(200).json({
            success: true,
            message: 'Category updated successfully',
            data: category
        });
    } catch (error) {
        next(error);
    }
};

// DELETE /api/categories/:id
exports.deleteCategory = async (req, res, next) => {
    try {
        const category = await Category.findByIdAndDelete(req.params.id);
        if (!category) {
            return res.status(404).json({
                success: false,
                message: 'No category found to delete'
            });
        }

        res.status(200).json({
            success: true,
            message: 'Category deleted successfully',
            data: category
        });
    } catch (error) {
        next(error);
    }
};
