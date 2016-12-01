enum FeatureTypeEnum {
    Jungle,
    Trail,
    Lake,
    Shore,
    TrailEnd,
    InnerShore,
    Den,
    None;

    public int toInt(){
        switch(this){
            case Jungle: return 0;
            case Trail: return 1;
            case Lake: return 2;
            case Shore: return 3;
            case TrailEnd: return 4;
            case InnerShore: return 5;
            case Den: return 6;
            case None: return 9;
            default: throw new IllegalStateException();
        }
    }

    public char toChar(){
        switch(this){
            case Jungle: return 'F';
            case Trail: return 'R';
            case TrailEnd: return 'E';
            case Lake: return 'C';
            case Shore: return 'W';
            case InnerShore: return 'I';
            case Den: return 'M';
            case None: return 'X';
            default: throw new IllegalStateException();
        }
    }

    public boolean isSameFeature(FeatureTypeEnum otherType){
        switch (this){
            case Jungle:
                return otherType == Jungle;
            case Trail:
            case TrailEnd:
                return (otherType == Trail || otherType == TrailEnd);
            case Lake:
            case Shore:
            case InnerShore:
                return (otherType == Lake || otherType == Shore || otherType == InnerShore);
            case Den:
                return (otherType == Den);
            case None:
                return (otherType == None);
            default: throw new IllegalStateException();
        }
    }

    public boolean isShoreToLake(FeatureTypeEnum otherType){
        switch (this){
            case Lake:
            case Shore:
                return (otherType == Lake || otherType == Shore);
            default:
                return false;
        }
    }
}
