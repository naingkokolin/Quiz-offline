import Database from "better-sqlite3";
const db = new Database('quiz.db');

const initDb = () => {
    const query = `
        CREATE TABLE IF NOT EXISTS questions (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          category TEXT NOT NULL,
          question TEXT NOT NULL,
          option_a TEXT NOT NULL,
          option_b TEXT NOT NULL,
          option_c TEXT NOT NULL,
          option_d TEXT NOT NULL,
          correct_answer TEXT NOT NULL,
          used INTEGER DEFAULT 0
        );
    `;
    db.exec(query);

    const historyQuery = `
        CREATE TABLE IF NOT EXISTS quiz_history (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          category TEXT NOT NULL,
          score INTEGER NOT NULL,
          total_questions INTEGER NOT NULL,
          timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
        );
    `;
    db.exec(historyQuery);

    const settingsQuery = `
        CREATE TABLE IF NOT EXISTS settings (
          key TEXT PRIMARY KEY,
          value TEXT
        );
    `;
    db.exec(settingsQuery);

    db.prepare("INSERT OR IGNORE INTO settings (key, value) VALUES (?, ?)").run("app_title", "Myanmar Quiz");
};

export { db };
export default initDb;