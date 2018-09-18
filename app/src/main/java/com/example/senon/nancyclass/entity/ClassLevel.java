package com.example.senon.nancyclass.entity;

import com.example.senon.nancyclass.R;

import java.util.ArrayList;

/**
 * 听课等级
 */
public class ClassLevel {

    private int imageCheck;//选中的图片资源id
    private int imageNoCheck;//未选中的图片资源id
    private boolean isCheck;//是否被选中
    private String des;//文字描述
    private int level;//等级  1优秀  2比较好 3良好  4一般  5较差

    public ClassLevel() {
    }

    public ClassLevel(boolean isCheck, String des, int level,int imageCheck, int imageNoCheck) {
        this.isCheck = isCheck;
        this.des = des;
        this.level = level;
        this.imageCheck = imageCheck;
        this.imageNoCheck = imageNoCheck;
    }

    public ArrayList getTypeTop(){
        ArrayList<ClassLevel> list = new ArrayList<>();
        list.add(new ClassLevel(false,"优秀",1,R.drawable.smile11, R.drawable.smile1));
        list.add(new ClassLevel(false,"优秀",1,R.drawable.smile11, R.drawable.smile1));
        list.add(new ClassLevel(false,"优秀",1,R.drawable.smile11, R.drawable.smile1));
        list.add(new ClassLevel(false,"优秀",1,R.drawable.smile11, R.drawable.smile1));
        list.add(new ClassLevel(false,"优秀",1,R.drawable.smile11, R.drawable.smile1));

        return list;
    }

    public int getImageCheck() {
        return imageCheck;
    }

    public void setImageCheck(int imageCheck) {
        this.imageCheck = imageCheck;
    }

    public int getImageNoCheck() {
        return imageNoCheck;
    }

    public void setImageNoCheck(int imageNoCheck) {
        this.imageNoCheck = imageNoCheck;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
