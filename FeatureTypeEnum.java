public enum FeatureTypeEnum {
    Field,
    Road,
    RoadEnd,
    City,
    Wall,
    InnerWall,
    Monastery,
    None;

    public char print(){
        switch(this){
            case Field: return 'F';
            case Road: return 'R';
            case RoadEnd: return 'E';
            case City: return 'C';
            case Wall: return 'W';
            case InnerWall: return 'I';
            case Monastery: return 'M';
            case None: return 'X';
            default: throw new IllegalStateException();
        }
    }
}
