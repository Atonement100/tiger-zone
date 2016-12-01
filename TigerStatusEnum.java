public enum TigerStatusEnum {
        onJungle,
        onLake,
        onTrail,
        onDen,
        onNone;

        public int toInt(){
            switch (this){
                case onJungle: return 1;
                case onLake: return 2;
                case onTrail: return 3;
                case onDen: return 4;
                case onNone: return 0;
                default: throw new IllegalStateException();
            }
        }

}
