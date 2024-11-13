package please_do_it.yumi.constant;

public enum BusinessStatus {

  OPEN("영업 중") , CLOSE("영업 종료") , BREAK_TIME("브레이크 타임");

  private String description;

  BusinessStatus(String description){
    this.description = description;
  }

  public String getDescription(){
    return description
  }

}
