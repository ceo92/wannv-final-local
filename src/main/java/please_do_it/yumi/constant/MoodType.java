package please_do_it.yumi.constant;


public enum MoodType {

  DATE("데이트하기 좋은") , MODERN("모던한") , TRENDY("트랜디한") ,  FAMILY("가족회식하기 좋은") , MEETING("상견례") , CALM("차분한");

  private String description;

  //생성자이자 설정자 역할
  MoodType(String description){
    this.description = description;
  }


  //접근자 역할
  public String getDescription(){
    return description;
  }

}
