package com.example.senon.nancyclass.greendaoutil;


import com.example.senon.nancyclass.greendao.UserReviewDao;
import com.example.senon.nancyclass.greendaoentity.UserReview;
import java.util.List;


public class UserReviewDt {
    public UserReviewDt() {
    }

    public UserReview findByName(String name){
        List<UserReview> list_record = SqlEnu.Local.cn().getUserReviewDao().queryBuilder()
                .where(UserReviewDao.Properties.Name.eq(name)).list();
        return list_record.size() > 0 ? list_record.get(0) : null;
    }

    public List<UserReview> getAll() {
        return SqlEnu.Local.cn().getUserReviewDao().queryBuilder().list();
    }

    public void insert(UserReview bean) {
        if (findByName(bean.getName()) == null) {
            SqlEnu.Local.cn().getUserReviewDao().insert(bean);
        }
    }

    public void update(String name, int total, int lost, String time, UserReview bean) {
        UserReview UserReview = findByName(name);
        if(UserReview != null){
            bean.setSignTime(time);
            bean.setTotal(total);
            bean.setLast(lost);
        }
        SqlEnu.Local.cn().getUserReviewDao().update(bean);
    }

    public void update(UserReview bean) {
        SqlEnu.Local.cn().getUserReviewDao().update(bean);
    }

    public void delete(UserReview bean) {
        if (findByName(bean.getName()) != null) {
            SqlEnu.Local.cn().getUserReviewDao().delete(bean);
        }
    }

    public void delete(String name) {
        if (findByName(name) != null) {
            SqlEnu.Local.cn().getUserReviewDao().delete(findByName(name));
        }
    }

    public void deleteAll() {
        SqlEnu.Local.cn().getUserReviewDao().deleteAll();
    }

}
