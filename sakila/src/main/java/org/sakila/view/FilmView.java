package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.FilmDao;
import org.sakila.entity.Film;

import java.math.BigDecimal;
import java.util.Scanner;

public class FilmView implements View {

    Scanner in = new Scanner(System.in);
    FilmDao filmDao = new FilmDao();

    @Override
    public void indexWindow() {
        System.out.println("#########影片信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加影片");
        System.out.println("2.修改影片");
        System.out.println("3.删除影片");
        System.out.println("4.查询影片");
        System.out.println("5.返回上一级菜单");
        String select = in.nextLine();
        switch (select) {
            case "1":
                addWindow();
                break;
            case "2":
                updateWindow();
                break;
            case "3":
                deleteWindow();
                break;
            case "4":
                queryWindow();
                break;
            case "5":
                Controller.redirect("index");
                return;
            default:
                System.out.println("输入有误，请重新选择");
        }
        indexWindow();
    }

    public void addWindow() {
        System.out.println("-----添加影片-----");
        Film film = readFilmForAdd();
        filmDao.addFilm(film);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改影片-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int filmId = InputHelper.readRequiredInt(in, "请输入要修改的影片ID: ");
        Film old = filmDao.getFilmById(filmId);
        if (old == null) {
            System.out.println("影片不存在");
            return;
        }
        System.out.println("原信息: " + old);
        old.setTitle(InputHelper.keepString(in, "片名", old.getTitle()));
        old.setDescription(InputHelper.keepString(in, "简介", old.getDescription()));
        old.setReleaseYear(InputHelper.keepInteger(in, "上映年份", old.getReleaseYear()));
        System.out.println("可选语言列表:");
        filmDao.listLanguages();
        old.setLanguageId(InputHelper.keepInt(in, "语言编号", old.getLanguageId()));
        old.setRentalDuration(InputHelper.keepInt(in, "租期(天)", old.getRentalDuration()));
        old.setRentalRate(InputHelper.keepDecimal(in, "租金", old.getRentalRate()));
        old.setLength(InputHelper.keepInteger(in, "片长(分钟)", old.getLength()));
        old.setReplacementCost(InputHelper.keepDecimal(in, "重置成本", old.getReplacementCost()));
        old.setRating(InputHelper.keepString(in, "分级", old.getRating()));
        old.setFilmId(filmId);
        filmDao.updateFilm(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除影片-----");
        int filmId = InputHelper.readRequiredInt(in, "请输入要删除的影片ID: ");
        Film old = filmDao.getFilmById(filmId);
        if (old == null) {
            System.out.println("影片不存在");
            return;
        }
        filmDao.deleteFilm(filmId);
        System.out.println("删除成功: " + old.getTitle());
    }

    public void queryWindow() {
        System.out.println("-----查询影片-----");
        InputHelper.printQueryTip();
        Integer id = InputHelper.filterInt(in, "影片编号");
        String keyword = InputHelper.filterString(in, "片名关键字");
        Integer languageId = InputHelper.filterInt(in, "语言编号");
        InputHelper.printList(filmDao.selectByCondition(id, keyword, languageId), in);
    }

    private Film readFilmForAdd() {
        Film film = new Film();
        film.setTitle(InputHelper.readRequiredString(in, "[必填] 请输入片名: "));
        film.setDescription(InputHelper.readOptionalString(in, "[可空] 请输入简介: "));
        film.setReleaseYear(InputHelper.readOptionalInt(in, "[可空] 请输入上映年份(如2006): "));
        System.out.println("可选语言列表:");
        filmDao.listLanguages();
        film.setLanguageId(InputHelper.readRequiredInt(in, "[必填] 请输入语言ID: "));
        Integer duration = InputHelper.readOptionalInt(in, "[可空] 请输入租期天数(默认3): ");
        film.setRentalDuration(duration == null ? 3 : duration);
        film.setRentalRate(InputHelper.readOptionalDecimal(in, "[可空] 请输入租金(默认4.99): ", new BigDecimal("4.99")));
        film.setLength(InputHelper.readOptionalInt(in, "[可空] 请输入时长分钟: "));
        film.setReplacementCost(InputHelper.readOptionalDecimal(in, "[可空] 请输入替换费用(默认19.99): ", new BigDecimal("19.99")));
        System.out.print("[可空] 请输入分级(G/PG/PG-13/R/NC-17，默认G): ");
        String rating = in.nextLine();
        film.setRating(rating.isEmpty() ? "G" : rating);
        return film;
    }
}
