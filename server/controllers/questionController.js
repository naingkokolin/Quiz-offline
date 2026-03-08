import { db } from "../helpers/db.js";

export const createQuestion = (req, res) => {

    try{
        const {
          category,
          question,
          option_a,
          option_b,
          option_c,
          option_d,
          correct_answer,
        } = req.body;

        const sql = `
          INSERT INTO questions
          (category, question, option_a, option_b, option_c, option_d, correct_answer)
          VALUES (?, ?, ?, ?, ?, ?, ?)
        `;

        // better-sqlite3 uses .prepare().run() for INSERT/UPDATE
        const stmt = db.prepare(sql);
        stmt.run(category, question, option_a, option_b, option_c, option_d, correct_answer);

        res.json({ message: "Question added" });
    } catch (e) {
        res.status(500).send({message: `Something went wrong while creating a question: ${e.message}`});
    }
}

export const getAllQuestions = (req, res) => {
    try {
        const questions = db.prepare("SELECT * FROM questions").all();

        if (questions.length === 0) {
            return res.status(200).json({ message: "No questions found in database", data: [] });
        }

        res.json(questions);
    } catch (e) {
        res.status(500).json({ message: `Error fetching questions: ${e.message}` });
    }
};

export const getQuestionsByCategory = (req, res) => {
    try {
        const { category } = req.params;
        const questions = db.prepare("SELECT * FROM questions WHERE category = ?").all(category);
        res.json(questions);
    } catch (e) {
        res.status(500).json({ error: e.message });
    }
};

export const getRandomQuestion = (req, res) => {
    try{
       const { category } = req.params;

       const sql = `
         SELECT * FROM questions
         WHERE category = ? AND used = 0
         ORDER BY RANDOM() LIMIT 1
       `;

       const row = db.prepare(sql).get(category);

       if (!row) {
         return res.status(404).json({ message: "No unused questions found in this category" });
       }

       res.json(row);
    } catch (e) {
        return res.status(500).json({ message: `Something went wrong while getting a question: ${e.message}` });
    }
};

export const markQuestionAsUsed = (req, res) => {
    try {
        const { id } = req.params;

        const sql = `UPDATE questions SET used = 1 WHERE id = ?`;
        const stmt = db.prepare(sql);
        const result = stmt.run(id);

        if (result.changes === 0) {
            return res.status(404).json({ message: "Question not found" });
        }

        res.json({ message: "Question marked as used" });
    } catch (e) {
        res.status(500).json({ message: `Error updating question: ${e.message}` });
    }
};

export const deleteAllQuestions = (req, res) => {
    try {
        db.prepare("DELETE FROM questions").run();

        db.prepare("DELETE FROM sqlite_sequence WHERE name='questions'").run();

        res.json({ message: "All questions deleted. You can now add new ones!" });
    } catch (e) {
        res.status(500).json({ message: `Error clearing database: ${e.message}` });
    }
};

export const uploadBulkQuestions = (req, res) => {
    try {
        const questions = req.body;

        if (!Array.isArray(questions)) {
            return res.status(400).json({ error: "Data format must be an Array" });
        }

        const insert = db.prepare(`
            INSERT INTO questions (category, question, option_a, option_b, option_c, option_d, correct_answer)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        `);

        const insertMany = db.transaction((data) => {
            for (const q of data) {
                insert.run(q.category, q.question, q.option_a, q.option_b, q.option_c, q.option_d, q.correct_answer);
            }
        });

        insertMany(questions);

        res.json({ message: `${questions.length} questions uploaded successfully!` });
    } catch (e) {
        res.status(500).json({ error: `Bulk upload failed: ${e.message}` });
    }
};