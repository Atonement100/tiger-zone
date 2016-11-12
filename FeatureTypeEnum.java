public enum FeatureTypeEnum {
    Field,
    Road,
    City,
    Wall,
    Monastery,
    None;

    public char print(){
        switch(this){
            case Field: return 'F';
            case Road: return 'R';
            case City: return 'C';
            case Wall: return 'W';
            case Monastery: return 'M';
            case None: return 'X';
            default: throw new IllegalStateException();
        }
    }
}
