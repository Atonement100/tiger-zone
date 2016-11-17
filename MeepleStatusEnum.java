public enum MeepleStatusEnum {
        onField,
        onCity,
        onRoad,
        onMonastery,
        onNone;

        public int toInt(){
            switch (this){
                case onField: return 1;
                case onCity: return 2;
                case onRoad: return 3;
                case onMonastery: return 4;
                case onNone: return 0;
                default: throw new IllegalStateException();
            }
        }

}
