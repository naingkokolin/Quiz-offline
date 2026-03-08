import { db } from "../helpers/db.js";

export const saveQuizResult = (req, res) => {
    try {
        const { category, score, total } = req.body;
        const sql = `INSERT INTO quiz_history (category, score, total_questions) VALUES (?, ?, ?)`;

        db.prepare(sql).run(category, score, total);
        res.json({ message: "History saved successfully" });
    } catch (e) {
        res.status(500).json({ message: `Error saving history: ${e.message}` });
    }
};

export const getHistory = (req, res) => {
    try {
        const sql = `SELECT * FROM quiz_history ORDER BY timestamp DESC`;
        const logs = db.prepare(sql).all();
        res.json(logs);
    } catch (e) {
        res.status(500).json({ message: `Error fetching history: ${e.message}` });
    }
};