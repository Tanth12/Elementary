package src.lessons.lesson1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tanth on 08.07.17.
 */
public class RoomTest {
    private static final String ROOM_CLASS_NAME = "src.lessons.lesson1.Room";
    private static final List<String> INT_FIELD_NAMES = Arrays.asList("height", "width", "longitude");
    private static final String NAME_FIELD = "name";

    /**
     * Проверка существования класса
     */
    @Test
    public void testRoomClassExistence() {
        System.out.println("Ищу класс " + ROOM_CLASS_NAME);
        getRoomClass();
        System.out.println("Класс " + ROOM_CLASS_NAME + " найден");
    }

    /**
     * Проверка существования 2х конструкторов
     */
    @Test
    public void testConstructors(){
        System.out.println("Проверяю наличие и правильность конструкторов");
        Assertions.assertEquals(2, getRoomClass().getConstructors().length, "Неверное кол-во конструкторов в классе");
        getEmptyRoomConstructor();
        Constructor threeIntConstructor = Arrays.stream(getRoomClass().getConstructors())
                .filter(c -> c.getParameterTypes().length == 3)
                .findAny()
                .orElseThrow(() -> new AssertionError("Не найден конструктор c 3мя полями"));
        Class[] constructorParameterTypes = threeIntConstructor.getParameterTypes();
        Assertions.assertEquals(3, constructorParameterTypes.length, "Неверное кол-во аргументов в непустом конструкторе");
        boolean allAreInt = Arrays.stream(constructorParameterTypes)
                .map(par -> par.getName())
                .allMatch(pName -> "int".equals(pName));
        Assertions.assertTrue(allAreInt, "Неверный тип у одного из аргументов в непустом конструторе");
        System.out.println("Конструкторы проинициализированны верно");
    }

    /**
     * Проверка полей класса
     */
    @Test
    public void testRoomExistedFields() {
        System.out.println("Проверяю поля класса");
        Class roomClass = getRoomClass();
        Assertions.assertEquals(4, roomClass.getDeclaredFields().length, "Неверное кол-во полей в классе");
        for (String fieldName : INT_FIELD_NAMES){
            testIntPrivateField(fieldName, roomClass);
        }
        Field nameField = getField(NAME_FIELD, roomClass);
        Assertions.assertEquals(Modifier.PRIVATE, nameField.getModifiers(), "Неверно задан модификатор поля" + NAME_FIELD);
        Assertions.assertEquals(nameField.getType(), String.class, "Неверный тип поля" + NAME_FIELD);
        System.out.println("Поля класса заданы правильно");
    }


    @Test
    public void testRoomInit() {
        System.out.println("Проверка создания класса через пустой конструктор");
        Object emptyConstructorRoom = initRoom(null);
        checkAllRoomFields(emptyConstructorRoom, 0, 0, 0, null);
        System.out.println("Проверка создания класса через пустой конструктор завершена");
        System.out.println("Проверка создания класса через конструктор с 3мя аргументами");
        Object filledRoom = initRoom(new Object[]{2, 4, 5});
        checkAllRoomFields(filledRoom, 2, 4, 5, null);
        System.out.println("Проверка создания класса через конструктор с 3мя аргументами завершена");
    }

    @Test
    public void testMethodsExistence() {
        System.out.println("Проверка наличия методов");

        System.out.println("Проверка наличия геттеров");
        checkPublicMethodExistence(getGetters());
        System.out.println("Проверка наличия геттеров завершена");
        System.out.println("Проверка наличия сеттеров");
        checkPublicMethodExistence(getSetters());
        System.out.println("Проверка наличия сеттеров завершена");

        System.out.println("Проверка наличия дополнительных методов");
        checkPublicMethodExistence(Arrays.asList("getSquare", "getPerimeter", "getSpace"));
        System.out.println("Проверка наличия дополнительных методов завершена");

        System.out.println("Проверка наличия методов завершена");
    }

    @Test
    public void testGettersAndSetters() {
        System.out.println("Проверка геттеров и сеттеров");
        Object room = initRoom(null);
        checkAllRoomFields(room, 0,0,0, null);
        Random random = new Random();
        int height = random.nextInt(10);
        int width = random.nextInt(10);
        int longitude = random.nextInt(10);
        String name = UUID.randomUUID().toString();

        try {
            System.out.println("Проверка сеттеров");
            getMethodByName("setHeight").invoke(room, height);
            getMethodByName("setWidth").invoke(room, width);
            getMethodByName("setLongitude").invoke(room, longitude);
            getMethodByName("setName").invoke(room, name);
            checkAllRoomFields(room, height, width, longitude, name);
            System.out.println("Проверка сеттеров завершена");

            System.out.println("Проверка геттеров");
            checkGetter(room, "getHeight", height);
            checkGetter(room, "getWidth", width);
            checkGetter(room, "getLongitude", longitude);
            checkGetter(room, "getName", name);
            checkAllRoomFields(room, height, width, longitude, name);
            System.out.println("Проверка геттеров завершена");
        } catch (Exception e) {
            Assertions.fail("Не могу выполнить метод " + e);
        }
        System.out.println("Проверка геттеров и сеттеров завершена");
    }

    @Test
    public void testAddtitionalMethods(){
        System.out.println("Проверка остальных методов");
        Object room = initRoom(null);
        Random random = new Random();
        int height = random.nextInt(10);
        int width = random.nextInt(10);
        int longitude = random.nextInt(10);
        String name = UUID.randomUUID().toString();

        setField("height", room, height);
        setField("width", room, width);
        setField("longitude", room, longitude);
        setField("name", room, name);

        try {
            System.out.println("Проверка метода вычисления площади");
            Object getSquare = getMethodByName("getSquare").invoke(room, null);
            Assertions.assertEquals(width * longitude, getSquare, "Неверно выполнилась ф-я вычисления площади");
            System.out.println("Проверка метода вычисления площади завершена");

            System.out.println("Проверка метода вычисления периметра");
            Object getPerimeter = getMethodByName("getPerimeter").invoke(room, null);
            Assertions.assertEquals(2 * (width + longitude), getPerimeter, "Неверно выполнилась ф-я вычисления периметра");
            System.out.println("Проверка метода вычисления периметра завершена");

            System.out.println("Проверка метода вычисления объема");
            Object getSpace = getMethodByName("getSpace").invoke(room, null);
            Assertions.assertEquals(width * longitude * height, getSpace, "Неверно выполнилась ф-я вычисления объема");
            System.out.println("Проверка метода вычисления объема завершена");

            System.out.println("Проверка метода toString()");
            String roomString = MessageFormat.format("Room.name={0};Room.square={1};Room.perimeter={2}",
                                                    name,
                                                    getSquare,
                                                    getPerimeter);

            Assertions.assertEquals(roomString, room.toString(), "Неверно выполнился метод toString");
            System.out.println("Проверка метода toString() завершена");
        } catch (Exception e) {
            Assertions.fail("Не могу выполнить метод " + e);
        }

        System.out.println("Проверка остальных методов завершена");
    }

    private List<String> getCapitalLettersFieldNames(){
        List<String> allFields = new ArrayList<>();
        allFields.addAll(INT_FIELD_NAMES);
        allFields.add(NAME_FIELD);
        return allFields.stream()
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                .collect(Collectors.toList());
    }

    private void checkGetter(Object room, String getterFullName, Object value) throws Exception {
        Object getterValue = getMethodByName(getterFullName).invoke(room);
        Assertions.assertEquals(value, getterValue, "Значение геттера не совпадает с исходным");
    }
    private List<String> getGetters(){
        return getCapitalLettersFieldNames().stream().map(s -> "get" + s).collect(Collectors.toList());
    }

    private List<String> getSetters(){
        return getCapitalLettersFieldNames().stream().map(s -> "set" + s).collect(Collectors.toList());
    }

    private void checkPublicMethodExistence(List<String> methodNames){
        for(String methodName : methodNames) {
            Method method = getMethodByName(methodName);
            Assertions.assertEquals(Modifier.PUBLIC,
                    method.getModifiers(),
                    "Неправильный модификатор доступа для метода " + methodName);
        }
    }

    private Method getMethodByName(String methodName){
        return Arrays.stream(getRoomClass().getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .findAny()
                .orElseThrow(() -> new AssertionError("Не могу найти метод " + methodName));
    }

    private void checkAllRoomFields(Object room, int height, int width, int longitude, String name){
        checkField(room, "height", height);
        checkField(room, "width", width);
        checkField(room, "longitude", longitude);
        checkField(room, "name", name);
    }

    private void checkField(Object object, String fieldName, Object value){
        Field field = getField(fieldName, object.getClass());
        Object fieldValue = null;
        try {
            field.setAccessible(true);
            fieldValue = field.get(object);
        } catch (IllegalAccessException e) {
            Assertions.fail("Не могу получить доступ к полю " + fieldName);
        }
        Assertions.assertEquals(value, fieldValue, "Значения поля " + fieldName + " не совпадают");
    }

    private void setField(String fieldName, Object room, Object value){
        Field field = getField(fieldName, getRoomClass());
        field.setAccessible(true);
        try {
            field.set(room, value);
        } catch (IllegalAccessException e) {
            Assertions.fail("Не могу задать значение " + value + "полю " + fieldName + "\n" + e);
        }
    }

    private void testIntPrivateField(String fieldName, Class clazz) {
        Field field = getField(fieldName, clazz);
        Assertions.assertEquals(Modifier.PRIVATE, field.getModifiers(), "Неверно задан модификатор поля" + fieldName);
        Assertions.assertEquals("int", field.getType().getName(), "Неверный тип поля " + fieldName);
    }

    private Object initRoom(Object[] args){
        int argsLength = args == null ? 0 : args.length;
        Constructor constructor = Arrays.stream(getRoomClass().getConstructors())
                .filter(c -> c.getParameterTypes().length == argsLength)
                .findAny()
                .orElseThrow(() -> new AssertionError("Не найден конструктор для " + args.length + " аргументов"));
        Object o = null;
        try {
            o = constructor.newInstance(args);
        } catch (Exception e) {
            Assertions.fail("Не могу создать класс room с пустым конструктором" + e);
        }
        return o;
    }

    private Constructor getEmptyRoomConstructor(){
        Class clazz = getRoomClass();
        return Arrays.stream(clazz.getConstructors())
                .filter(c -> c.getParameterTypes().length == 0)
                .findAny()
                .orElseThrow(() -> new AssertionError("Не найден конструктор по умолчанию (без аргументов)"));
    }

    private Field getField(String fieldName, Class clazz){
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e){
            System.out.println("Поле " + fieldName + " не найдено");
            Assertions.fail("Поле " + fieldName + " не найдено");
        }
        throw new RuntimeException("Should never happen");
    }


    private Class getRoomClass() {
        try {
            return Class.forName(ROOM_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            String message = "Класс " + ROOM_CLASS_NAME + " не найден";
            System.out.println(message);
            Assertions.fail(message);
        }
        throw new RuntimeException();
    }
}
