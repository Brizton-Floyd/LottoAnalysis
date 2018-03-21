import java.util.*;
import java.util.stream.*;
import com.lottoanalysis.lottogames.LottoGame;

public class NumberMultipleAnalyzer {
    
    private Map<Integer,NumberMultipleAnalyzer> multipleHitOccurenceHolder = new TreeMap<>();
    private Map<Integer,Integer[]> lottoNumberHitOccurenceHolder = new TreeMap<>();
    private int multipleHits;
    private int multipleGamesOut;
    private int multipleHitsAtGamesOut;
    private int multipleGameOutLastAppearance;
    private List<Integer> gamesOutHolder = new ArrayList<>();

    private static Map<Integer,List<Integer>> multipleRanges = new LinkedHashMap<>();
    static
    {
        multipleRanges.put(7, new ArrayList<>());
        multipleRanges.put(5, new ArrayList<>());
        multipleRanges.put(3, new ArrayList<>());
        multipleRanges.put(2, new ArrayList<>());
        multipleRanges.put(1, new ArrayList<>());
    }

    private NumberMultipleAnalyzer(){
        this.multipleGamesOut = -1;
        this.multipleHits = -1;
        this.multipleHitsAtGamesOut = -1;
        this.multipleGameOutLastAppearance = -1;
    }

    public NumberMultipleAnalyzer(LottoGame game){

        popluateRangeBuckets(game.getMinNumber(), game.getMaxNumber());
    }

    // getters and setters
    public void setMultipleHits( int val ){
        this.multipleHits = val;
    }
    public void setMultipleHitsAtGamesOut( int val ){
        this.multipleHitsAtGamesOut = val;
    }
    public void setMultipleGameOutLastAppearance( int val ){
        this.multipleGameOutLastAppearance = val;
    }
    public void setMultipleGamesOut( int val ){
        this.multipleGamesOut = val;
    }
    public int getMultipleHits(){
        return this.multipleHits;
    }
    public int getMultipleGamesOut(){
        return this.multipleGamesOut;
    }
    public int getMultipleHitsAtGamesOut(){
        return this.multipleHitsAtGamesOut;
    }
    public int getMultipleGameOutLastAppearance(){
        return this.multipleGameOutLastAppearance;
    }
    private List<Integer> getGameOutHolder(){
        return gamesOutHolder;
    }
    private Map<Integer,Integer[]> getLottoNumberHitOccurenceHolder(){
        return this.lottoNumberHitOccurenceHolder;
    }
    public void print(){
        
        multipleHitOccurenceHolder.forEach( (k,v) -> {
            
            System.out.printf("\n<----- Multiple: %s %10s %4s %15s %4s %15s %4s %15s %4s ----->", k,"Hits:",v.getMultipleHits(),"Games Out:", v.getMultipleGamesOut(), 
            "Hits @ G Out:", v.getMultipleHitsAtGamesOut(), "Last Seen:",v.getMultipleGameOutLastAppearance());

            System.out.println();
            
            Map<Integer,Integer[]> numData = v.getLottoNumberHitOccurenceHolder();
            numData.forEach((lottoNumber,hitHolder) -> {
                System.out.printf("\nLotto #: %3s %9s %4s %15s %4s",lottoNumber,"Hits:",hitHolder[0],"Games Out:",hitHolder[1]);
            });
                        
            System.out.println();
        });
    }
    /**
     * Method will take in a lotto number and analyze it for the multiple it represents and then that value will be added to map 
     * for storage
     */
    public void analyzeLottoNumber( final int number ){

        Set<Integer> keys = new LinkedHashSet<>( multipleRanges.keySet() );
        for(Iterator<Integer> data = keys.iterator(); data.hasNext();){

            int key = data.next();
            List<Integer> numbersHolder = multipleRanges.get( key );
            if(numbersHolder.contains( number )){

                if(!multipleHitOccurenceHolder.containsKey( key )){

                    NumberMultipleAnalyzer analyzer = new NumberMultipleAnalyzer();
                    analyzer.setMultipleHits(1);
                    analyzer.setMultipleGamesOut(0);
                    multipleHitOccurenceHolder.put(key, analyzer);
                    incrementGamesOut( multipleHitOccurenceHolder, key);

                    analyzer.lottoNumberHitOccurenceHolder.put(number, new Integer[]{1,0,0,0});
                }
                else{

                    NumberMultipleAnalyzer analyzer = multipleHitOccurenceHolder.get( key );
                    int hits = analyzer.getMultipleHits();
                    analyzer.setMultipleHits(++hits);
                    analyzer.gamesOutHolder.add( analyzer.getMultipleGamesOut() );
                    analyzer.setMultipleGamesOut( 0 );
                    incrementGamesOut(multipleHitOccurenceHolder, key); 

                    Map<Integer,Integer[]> lottoNumData = analyzer.getLottoNumberHitOccurenceHolder();
                    if(!lottoNumData.containsKey(number)){
                        
                        lottoNumData.put(number, new Integer[]{1,0,0,0});
                        incrementLottoNumberGamesOut( lottoNumData, number);
                    }
                    else{
                        Integer[] numData = lottoNumData.get(number);
                        numData[0]++;
                        numData[1] = 0;
                        incrementLottoNumberGamesOut( lottoNumData, number);
                    }
                }

                break;
            }

        }
    }

    private void incrementLottoNumberGamesOut( Map<Integer,Integer[]> data, int winningNumber){
        
        data.forEach( (k,v) -> {
           
           if(k != winningNumber){
               v[1]++;
           } 
        });
    }
    /**
     * Will increment the games out for all non winning multiples 
     */
    private void incrementGamesOut( Map<Integer,NumberMultipleAnalyzer> data, int winningMultiple){

        data.forEach((k,v) -> {

            if(k != winningMultiple){
                int gamesOut = v.getMultipleGamesOut();
                v.setMultipleGamesOut( ++gamesOut );
            }
        });
    }

    public void computeHitsAtGamesOutAndLastAppearance(){
        
        multipleHitOccurenceHolder.forEach( (k,v) -> {
           
           List<Integer> gameOutHolderr = v.getGameOutHolder();
           
           long val = gameOutHolderr.stream().filter( i -> i == v.getMultipleGamesOut() ).count();
           v.setMultipleHitsAtGamesOut( (int)val );
           
           int lastAppear = Math.abs( gameOutHolderr.size() - gameOutHolderr.lastIndexOf(v.getMultipleGamesOut() ));
           v.setMultipleGameOutLastAppearance( lastAppear );
        });
    }
    /*
     *
     * Method populates the range buckets into certain multiple buckets 
     */
    private void popluateRangeBuckets(int min, int max){
        
        List<Integer> gameNumRangeHolder = new ArrayList<>();
        
        for(int i = min; i <= max; i++)
        {
            gameNumRangeHolder.add( i );
        }

        final Set<Integer> set = new LinkedHashSet<>( multipleRanges.keySet() );
        
        for(int num : gameNumRangeHolder){
            
            for(Iterator<Integer> multiple = set.iterator(); multiple.hasNext();){
                
                int mult = multiple.next();
                int remainder = num % mult; 
                if(remainder == 0){
                    
                    List<Integer> holder = multipleRanges.get( mult );
                    holder.add( num );
                    break;
                }
            }
        }        
    }
}
