package com.springboot.demo.ioc;

public class QQQ {
     WWW www;


     public QQQ(){

     }
    public QQQ(WWW www){
        this.www = www;
    }
    public void qqq(){
        www.www();
    }

    public static void main(String[] args) {
        QQQ qqq = new QQQ();
        WWW www = new WWW();
        qqq.www = www;
        www.qqq = qqq;
    }
}

class WWW{
     QQQ qqq;
    public WWW(){

    }
    public WWW(QQQ qqq){
        this.qqq = qqq;
    }
    public void www(){
        System.out.println("www");
    }

}
