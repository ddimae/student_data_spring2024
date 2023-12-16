package ntukhpi.semit.dde.studentsdata.files;

import ntukhpi.semit.dde.studentsdata.entity.*;
import ntukhpi.semit.dde.studentsdata.service.interf.AcademicGroupService;
import ntukhpi.semit.dde.studentsdata.service.interf.AddressService;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.service.interf.StudentService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ntukhpi.semit.dde.studentsdata.utils.Language.EN;

public class ExcelUtilities {
    public static final String STUDENTSDATA_FILES_FOLDER = "students_data/";

    public static final String ATTACHMENT_FILENAME = "attachment; filename=\"";

    public static AcademicGroup readFromWBExcel(String filename) {
        AcademicGroup group;
        Path path = Paths.get(filename); //отримуємо шлях до файлу
        try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // отримуємо перший аркуш

            String fileName = path.getFileName().toString();

            group = new AcademicGroup(fileName.substring(0, fileName.length() - 5));

            for (Row row : sheet) {
                Student student = new Student(row);
                group.addStudent(student);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return group;
    }

    public static AcademicGroup readFromWBExcelFullToClearDB(String filename) {
        AcademicGroup group;
        Path path = Paths.get(filename); //отримуємо шлях до файлу
        try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // отримуємо перший аркуш

            String fileName = path.getFileName().toString();

            group = new AcademicGroup(fileName.substring(0, fileName.length() - 5));

            for (Row row : sheet) {
                //Student student = new Student(row);
                //Комірка 1 - ПІБ
                String[] nameParts = row.getCell(1).getStringCellValue().split(" ");
                Student student = new Student(nameParts[0], nameParts[1], nameParts[2]);
                //Комірка 2 - email in Office365
                Email khpiEmail = new Email(true, true, student, row.getCell(2).getStringCellValue());
                //Комірка 3 - personal email
                Email personalEmail = new Email(true, false, student, row.getCell(3).getStringCellValue());
                //Комірка 4 - phonesNumbers
                List<PhoneNumber> phones = parsePhones(row.getCell(4).getStringCellValue(), student);
                //Збираємо студента
                student.addContact(khpiEmail);
                student.addContact(personalEmail);
                phones.forEach(student::addContact);
                group.addStudent(student);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return group;
    }

    public static List<AcademicGroup> readAllGroupsFromExcel(String filename) {
        String groupPattern = "КН-";
        String studentPattern = "^\\S+\\s+\\S+\\s+\\S+$";
        Pattern pattern = Pattern.compile(groupPattern);
        Matcher matcher;

        List<AcademicGroup> groups = new ArrayList<>();
        Path path = Paths.get(filename); //отримуємо шлях до файлу
        try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // отримуємо перший аркуш

            String fileName = path.getFileName().toString();

            AcademicGroup groupCurrent = null;

            for (Row row : sheet) {
                //try {
                if(row.getCell(0)!=null) {
                    switch (row.getCell(0).getCellType()) {
                        case STRING:
                            System.out.println(row.getCell(0).getStringCellValue());
                            if (row.getCell(0).getStringCellValue().contains(groupPattern)) {
                                if (groupCurrent != null) {
                                    groups.add(groupCurrent);
                                }
                                groupCurrent = new AcademicGroup(row.getCell(0).getStringCellValue().split("\\s")[0]);
                                if (groupCurrent.getGroupName().contains(".е") || groupCurrent.getGroupName().contains("і"))
                                    groupCurrent.setLanguage(EN);
                            }
                            break;
                        case NUMERIC:
                            if (groupCurrent != null) {
                                Student student;
                                String[] nameParts = row.getCell(1).getStringCellValue().split(" ");
                                if(nameParts.length==3) student = new Student(nameParts[0], nameParts[1], nameParts[2]);
                                else if(nameParts.length==2)student = new Student(nameParts[0], nameParts[1], "NULL");
                                else{
                                    String LName = nameParts[0];
                                    StringBuilder MName = new StringBuilder(nameParts[1]);
                                    for (int i = 2; i < nameParts.length-1; i++){
                                        MName.append(" ").append(nameParts[i]);
                                    }
                                    String FName = nameParts[nameParts.length-1];
                                    student = new Student(LName,MName.toString(),FName);
                                }
                                //Комірка 2 - email in Office365
                                Email khpiEmail = new Email(true, true, student, row.getCell(2).getStringCellValue());
                                //Комірка 3 - personal email row.getCell(4) != null && !
                                if(row.getCell(3).getStringCellValue().contains("@")) {
                                    Email personalEmail = new Email(true, false, student, row.getCell(3).getStringCellValue());
                                    //Комірка 4 - phonesNumbers
                                    Cell phonecell = row.getCell(4);
                                    List<PhoneNumber> phones = new ArrayList<>();
                                    if (phonecell.getCellType() == CellType.NUMERIC)
                                        phones = (parsePhones(String.valueOf(phonecell.getNumericCellValue()), student));
                                    else if (phonecell.getCellType() == CellType.STRING)
                                        phones = parsePhones(row.getCell(4).getStringCellValue(), student);
                                    //Збираємо студента
                                    student.setAcademicGroup(groupCurrent);
                                    student.addContact(khpiEmail);
                                    student.addContact(personalEmail);
                                    System.out.println(student.getContacts());
                                    phones.forEach(student::addContact);
                                    groupCurrent.addStudent(student);
                                }
                                else {
                                    student.setAcademicGroup(groupCurrent);
                                    student.addContact(khpiEmail);
                                    groupCurrent.addStudent(student);
                                }
                            }
                            break;
                        default:
                            // Handle other cell types if necessary
                            break;
                    }
                }
//                }
//                catch (IllegalStateException e){ System.out.println("Whoops! " + e.getMessage());}
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return groups;
    }

    private static List<PhoneNumber> parsePhones(String row, Student owner) {
        final String regex = "\\+(380)\\((\\d{2})\\)-(\\d{3})-(\\d{2})-(\\d{2})";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(row);
        List<PhoneNumber> phones = new ArrayList<>();
        StringBuilder sb;
        while (matcher.find()) {
            sb = new StringBuilder();
            //System.out.println("Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                sb.append(matcher.group(i));
            }
            phones.add(new PhoneNumber(true, false, owner, sb.toString()));
        }
        return phones;
    }

    public static String saveToWBExcelWithName(String fullSavePath, String groupName, AcademicGroup academicGroup, String type) throws IOException {
        Set<Student> studentList = academicGroup.getStudentsList();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(groupName);

        // Стилі комірок
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setBorderTop(BorderStyle.THIN);
        boldStyle.setBorderBottom(BorderStyle.THIN);
        boldStyle.setBorderLeft(BorderStyle.THIN);
        boldStyle.setBorderRight(BorderStyle.THIN);
        boldStyle.setFont(boldFont);
        boldStyle.setAlignment(HorizontalAlignment.CENTER);

        int headerRows = 0;
        // add header
        switch (type) {
            case "F2" -> {
                headerRows = 2;
                Row row0 = sheet.createRow(0);
                Cell cell0 = row0.createCell(0);
                cell0.setCellStyle(style);
                cell0.setCellStyle(boldStyle);
                cell0.setCellValue("СПИСОК СТУДЕНТІВ НАЧАЛЬНОЇ ГРУПИ " + groupName + " (дата)");
                CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, 4);
                sheet.addMergedRegion(mergedRegion);

                String[] headers = {"№", "ПІПб", "Дата народження", "Бюджет/Контракт", "Стипендія"};

                Row row1 = sheet.createRow(1);
                int cellIndex = 0;
                for (String header : headers) {
                    Cell cell = row1.createCell(cellIndex++);
                    cell.setCellStyle(style);
                    cell.setCellValue(header);

                }
            }
        }
        //Створення рядків із даними студентів групи
        int rowIndex = 0;
        List<Student> sortStudentsList = studentList.stream().sorted((s1, s2) -> {
            int res = s1.getLastName().compareTo(s2.getLastName());
            if (res != 0) {
                return s1.getLastName().compareTo(s2.getLastName());
            } else {
                return s1.getFirstName().compareTo(s2.getFirstName());
            }
        }).toList();
        for (Student student : sortStudentsList) {
            Row row = sheet.createRow(headerRows + rowIndex++);

            Cell number = row.createCell(0);
            number.setCellStyle(style);
            number.setCellValue(rowIndex);

            switch (type) {
                case "F1" -> addRowByForm1(row, student, style);
                case "F2" -> addRowByForm2(row, student, academicGroup.isHead(student), style, boldStyle);
                case "F3" -> addRowByForm3(row, student, academicGroup.isHead(student), style, boldStyle, boldFont);
                case "F4" -> addRowByForm4(row, student, academicGroup.isHead(student), style, boldStyle, boldFont);
            }
        }


        Row firstRow = sheet.getRow(0); // Отримуємо перший рядок таблиці
        int numberOfColumns = firstRow.getLastCellNum(); // Отримуємо кількість використаних стовпців

        // Налаштуйте автоматичну ширину для кожного стовпця
        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
        //записати у файл
//        Path filePath = Paths.get(RESULTS_FOLDER + groupName + "_" + type + ".xlsx");//
        Path filePath = Paths.get(fullSavePath + groupName + "_" + type + ".xlsx");//
        try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            workbook.write(outputStream);
        }
        workbook.close();

        return filePath.toAbsolutePath().toString();
    }

    public static void addRowByForm1(Row row, Student student, CellStyle style) {

        Cell lastNameCell = row.createCell(1);
        lastNameCell.setCellStyle(style);
        lastNameCell.setCellValue(student.getLastName().toUpperCase());

        Cell firstNameCell = row.createCell(2);
        firstNameCell.setCellStyle(style);
        firstNameCell.setCellValue(student.getFirstName());

        Cell middleNameCell = row.createCell(3);
        middleNameCell.setCellStyle(style);
        middleNameCell.setCellValue(student.getMiddleName());
    }

    public static void addRowByForm2(Row row, Student student, boolean isHead, CellStyle style, CellStyle boldStyle) {
        createFullNameCell(row, student, isHead, style, boldStyle);

        Cell dateOfBirthCell = row.createCell(2);
        dateOfBirthCell.setCellStyle(style);
        dateOfBirthCell.setCellValue(student.formattedBirthday());

        Cell isContractCell = row.createCell(3);
        isContractCell.setCellStyle(style);
        isContractCell.setCellValue(student.isContractToString());

        Cell isTakeScholarshipCell = row.createCell(4);
        isTakeScholarshipCell.setCellStyle(style);
        isTakeScholarshipCell.setCellValue(student.isTakeScholarshipToString());
    }

    public static void addRowByForm3(Row row, Student student, boolean isHead, CellStyle style, CellStyle boldStyle, Font boldFont) {
        createFullNameCell(row, student, isHead, style, boldStyle);

        Cell dateOfBirthCell = row.createCell(2);
        dateOfBirthCell.setCellStyle(style);
        dateOfBirthCell.setCellValue(student.formattedBirthday());

        Set<Contact> contacts = student.getContacts();

        Cell phoneCell = row.createCell(3);
        phoneCell.setCellStyle(style);
        phoneCell.setCellValue(getStringOfPhones(boldFont, contacts));

        Cell emailCell = row.createCell(4);
        emailCell.setCellStyle(style);
        emailCell.setCellValue(getStringOfEmails(boldFont, contacts));

    }

    public static void addRowByForm4(Row row, Student student, boolean isHead, CellStyle style, CellStyle boldStyle, Font boldFont) {
        createFullNameCell(row, student, isHead, style, boldStyle);

        Set<Contact> contacts = student.getContacts();

        Cell phoneCell = row.createCell(2);
        phoneCell.setCellStyle(style);
        phoneCell.setCellValue(getStringOfPhones(boldFont, contacts));

        Map<Address, Boolean> addresses = student.getAddresses();

        Address firstTrueAddress = addresses.entrySet()
                .stream()
                .filter(entry -> entry.getValue()) // Фільтруємо записи за значенням true
                .map(Map.Entry::getKey) // Отримуємо ключ (Address)
                .findFirst() // Отримуємо перший ключ
                .orElse(null); // За замовчуванням, якщо ключ не знайдено

        if (firstTrueAddress != null) {
            System.out.println(firstTrueAddress);
            Cell adressCell = row.createCell(3);
            adressCell.setCellStyle(style);
            adressCell.setCellValue(firstTrueAddress.toString());
        }
    }

    private static void createFullNameCell(Row row, Student student, boolean isHead, CellStyle style, CellStyle boldStyle) {
        boldStyle.setAlignment(HorizontalAlignment.LEFT);

        Cell fullNameCell = row.createCell(1);
        fullNameCell.setCellStyle(isHead ? boldStyle : style);
        fullNameCell.setCellValue(student.fullNameToString() + (isHead ? " (староста)" : ""));
    }

    private static RichTextString getStringOfEmails(Font boldFont, Set<Contact> contacts) {
        Set<Email> emails = contacts.stream()
                .filter(contact -> contact instanceof Email)
                .map(contact -> (Email) contact)
                .collect(Collectors.toSet());

        String joinedEmails = emails.stream()
                .map(Email::getEmail)
                .collect(Collectors.joining(", "));

        Email mainEmail = null;
        for (Email email : emails) {
            if (email.isPrior()) {
                mainEmail = email;
                break;
            }
        }

        RichTextString richTextEmails = new XSSFRichTextString(joinedEmails);

        if (mainEmail != null) {
            String mainEmailText = mainEmail.getEmail();

            int startIndex = joinedEmails.indexOf(mainEmailText);
            int endIndex = startIndex + mainEmailText.length();
            richTextEmails.applyFont(startIndex, endIndex, boldFont);
        }
        return richTextEmails;
    }

    private static RichTextString getStringOfPhones(Font boldFont, Set<Contact> contacts) {
        Set<PhoneNumber> phoneNumbers = contacts.stream()
                .filter(contact -> contact instanceof PhoneNumber)
                .map(contact -> (PhoneNumber) contact)
                .collect(Collectors.toSet());

        String joinedNumbers = phoneNumbers.stream()
                .map(PhoneNumber::getPhoneNumber)
                .collect(Collectors.joining(", "));

        PhoneNumber mainPhoneNumber = null;
        for (PhoneNumber phoneNumber : phoneNumbers) {
            if (phoneNumber.isPrior()) {
                mainPhoneNumber = phoneNumber;
                break;
            }
        }

        RichTextString richTextNumbers = new XSSFRichTextString(joinedNumbers);

        if (mainPhoneNumber != null) {
            String mainNumber = mainPhoneNumber.getPhoneNumber();

            int startIndex = joinedNumbers.indexOf(mainNumber);
            int endIndex = startIndex + mainNumber.length();
            richTextNumbers.applyFont(startIndex, endIndex, boldFont);
        }

        return richTextNumbers;
    }


    /**
     * @param s - input string
     * @return - string ready to send via http
     */
    public static String rfc5987_encode(final String s) {
        final byte[] s_bytes = s.getBytes(StandardCharsets.UTF_8);
        final int len = s_bytes.length;
        final StringBuilder sb = new StringBuilder(len << 1);
        final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final byte[] attr_char = {'!', '#', '$', '&', '+', '-', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '~'};
        for (final byte b : s_bytes) {
            if (Arrays.binarySearch(attr_char, b) >= 0)
                sb.append((char) b);
            else {
                sb.append('%');
                sb.append(digits[0x0f & (b >>> 4)]);
                sb.append(digits[b & 0x0f]);
            }
        }

        return sb.toString();
    }

}
