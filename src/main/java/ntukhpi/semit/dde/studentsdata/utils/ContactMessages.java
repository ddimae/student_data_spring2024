package ntukhpi.semit.dde.studentsdata.utils;

public enum ContactMessages {
    EMPTY(""),
    MESSAGE01("Новий контакт був доданий!"),
    MESSAGE02("Помилка додавання! Insert SQL mistake!!!"),
    MESSAGE03("\"Неактивний\" контакт не може бути \"Основним\"!!!"),
    MESSAGE04("Помилка скидання Основного контакт! Update SQL mistake!!!"),
    MESSAGE05("Зробити \"Неактивний\" контакт \"Основним\" неможливо !!!"),
    MESSAGE06("\"Основний\" контакт вилучено!"),
    MESSAGE07("\"Основний\" контакт змінено!"),
    MESSAGE08("Помилка оновлення! Перевірте наявність контакт із статусом \"Основний\"! Update SQL mistake!!!"),
    MESSAGE09("Зробити  \"Основний\" контакт \"Неактивним\" неможливо !!!"),
    MESSAGE10("Статус контакт змінений!"),
    MESSAGE11("Помилка оновлення статусу актуальності! Update SQL mistake!!!"),
    MESSAGE12("Контакт було вилучено!!!"),
    MESSAGE13("Помилка вилучення! Delete SQL mistake!!!");

    private String text;

    ContactMessages(String s) {
        text = s;
    }

    public String getText() {
        return text;
    }
}
