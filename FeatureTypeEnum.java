public enum FeatureTypeEnum {
    Field,
    Road,
    City,
    Wall,
    RoadEnd,
    InnerWall,
    Monastery,
    None;

    public int toInt(){
        switch(this){
            case Field: return 0;
            case Road: return 1;
            case City: return 2;
            case Wall: return 3;
            case RoadEnd: return 4;
            case InnerWall: return 5;
            case Monastery: return 6;
            case None: return 9;
            default: throw new IllegalStateException();
        }
    }

    public char toChar(){
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
