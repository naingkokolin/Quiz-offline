import { db } from "../helpers/db.js";

export const getSettings = (req, res) => {
    try {
        const titleRow = db.prepare("SELECT value FROM settings WHERE key = 'app_title'").get();

        res.json({
            title: titleRow ? titleRow.value : "Myanmar Quiz",
            logoUrl: "/uploads/current-logo"
        });
    } catch (e) {
        res.status(500).json({ error: e.message });
    }
};

export const updateSettings = (req, res) => {
    try {
        const { title } = req.body;

        if (title) {
            db.prepare("INSERT OR REPLACE INTO settings (key, value) VALUES ('app_title', ?)").run(title);
        }

        res.json({
            message: "Settings updated successfully!",
            logoStatus: req.file ? "Logo uploaded" : "No logo uploaded"
        });
    } catch (e) {
        res.status(500).json({ error: e.message });
    }
};