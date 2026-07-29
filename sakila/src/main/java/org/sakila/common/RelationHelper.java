package org.sakila.common;

import org.sakila.dao.*;
import org.sakila.entity.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

/**
 * 查询结果附带关联表展示：按外键收集关联记录，相同主键只显示一次。
 */
public final class RelationHelper {

    private RelationHelper() {
    }

    /**
     * 先分页显示主表，退出分页后显示去重后的关联表（关联表超过 10 条同样分页）。
     */
    public static void printWithRelations(List<?> list, Scanner in) {
        PageHelper.printPaged(list, in);
        printRelated(list, in);
    }

    @SuppressWarnings("unchecked")
    public static void printRelated(List<?> list, Scanner in) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Object first = list.get(0);
        if (first instanceof City) {
            printRelatedForCities((List<City>) list, in);
        } else if (first instanceof Address) {
            printRelatedForAddresses((List<Address>) list, in);
        } else if (first instanceof Store) {
            printRelatedForStores((List<Store>) list, in);
        } else if (first instanceof Staff) {
            printRelatedForStaff((List<Staff>) list, in);
        } else if (first instanceof Customer) {
            printRelatedForCustomers((List<Customer>) list, in);
        } else if (first instanceof Film) {
            printRelatedForFilms((List<Film>) list, in);
        } else if (first instanceof Payment) {
            printRelatedForPayments((List<Payment>) list, in);
        } else if (first instanceof Rental) {
            printRelatedForRentals((List<Rental>) list, in);
        } else if (first instanceof Country) {
            printRelatedForCountries((List<Country>) list, in);
        }
        // category / language：无外键关联业务表，不追加
    }

    private static void printRelatedForCities(List<City> list, Scanner in) {
        CountryDao countryDao = new CountryDao();
        printSection("国家", loadByIds(list, City::getCountryId, countryDao::getById), in);
    }

    private static void printRelatedForAddresses(List<Address> list, Scanner in) {
        CityDao cityDao = new CityDao();
        CountryDao countryDao = new CountryDao();
        List<City> cities = loadByIds(list, Address::getCityId, cityDao::getById);
        printSection("城市", cities, in);
        printSection("国家", loadByIds(cities, City::getCountryId, countryDao::getById), in);
    }

    private static void printRelatedForStores(List<Store> list, Scanner in) {
        StaffDao staffDao = new StaffDao();
        AddressDao addressDao = new AddressDao();
        printSection("员工(店长)", loadByIds(list, Store::getManagerStaffId, staffDao::getById), in);
        printSection("地址", loadByIds(list, Store::getAddressId, addressDao::getAddressById), in);
    }

    private static void printRelatedForStaff(List<Staff> list, Scanner in) {
        AddressDao addressDao = new AddressDao();
        StoreDao storeDao = new StoreDao();
        printSection("地址", loadByIds(list, Staff::getAddressId, addressDao::getAddressById), in);
        printSection("商店", loadByIds(list, Staff::getStoreId, storeDao::getById), in);
    }

    private static void printRelatedForCustomers(List<Customer> list, Scanner in) {
        AddressDao addressDao = new AddressDao();
        StoreDao storeDao = new StoreDao();
        printSection("商店", loadByIds(list, Customer::getStoreId, storeDao::getById), in);
        printSection("地址", loadByIds(list, Customer::getAddressId, addressDao::getAddressById), in);
    }

    private static void printRelatedForFilms(List<Film> list, Scanner in) {
        LanguageDao languageDao = new LanguageDao();
        printSection("语言", loadByIds(list, Film::getLanguageId, languageDao::getById), in);
    }

    private static void printRelatedForPayments(List<Payment> list, Scanner in) {
        CustomerDao customerDao = new CustomerDao();
        StaffDao staffDao = new StaffDao();
        RentalDao rentalDao = new RentalDao();
        printSection("客户", loadByIds(list, Payment::getCustomerId, customerDao::getById), in);
        printSection("员工", loadByIds(list, Payment::getStaffId, staffDao::getById), in);
        printSection("租赁", loadByIds(list, Payment::getRentalId, rentalDao::getById), in);
    }

    private static void printRelatedForRentals(List<Rental> list, Scanner in) {
        CustomerDao customerDao = new CustomerDao();
        StaffDao staffDao = new StaffDao();
        FilmDao filmDao = new FilmDao();
        printSection("客户", loadByIds(list, Rental::getCustomerId, customerDao::getById), in);
        printSection("员工", loadByIds(list, Rental::getStaffId, staffDao::getById), in);
        printSection("影片", loadByIds(list, Rental::getFilmId, filmDao::getFilmById), in);
    }

    private static void printRelatedForCountries(List<Country> list, Scanner in) {
        CityDao cityDao = new CityDao();
        Map<Integer, City> cityMap = new LinkedHashMap<>();
        for (Country c : list) {
            for (City city : cityDao.selectByCondition(null, null, c.getCountryId())) {
                cityMap.putIfAbsent(city.getCityId(), city);
            }
        }
        printSection("城市", new ArrayList<>(cityMap.values()), in);
    }

    private static <T, R> List<R> loadByIds(List<T> source,
                                            Function<T, Integer> idGetter,
                                            Function<Integer, R> loader) {
        Map<Integer, R> map = new LinkedHashMap<>();
        for (T item : source) {
            Integer id = idGetter.apply(item);
            if (id == null || id == 0 || map.containsKey(id)) {
                continue;
            }
            R related = loader.apply(id);
            if (related != null) {
                map.put(id, related);
            }
        }
        return new ArrayList<>(map.values());
    }

    private static void printSection(String tableCn, List<?> related, Scanner in) {
        if (related == null || related.isEmpty()) {
            return;
        }
        System.out.println();
        System.out.println(">>>>>>>>>> 关联【" + tableCn + "】（去重后共 " + related.size() + " 条） <<<<<<<<<<");
        PageHelper.printPaged(related, in);
    }
}
