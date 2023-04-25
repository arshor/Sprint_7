import java.util.List;

public class OrderGenerator {

    public static Order getConst() {

        final String firstName = "Иван";
        final String lastName = "Иванов";
        final String address = "Москва, ул. Дубнинская 1 - 1 ";
        final  String metroStation = "Селигерская";
        final String phone = "+79804561234";
        final int rentTime = 10;
        final String deliveryDate = "2023-05-01";
        final String comment = "No comment";

        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, null);
    }
}
