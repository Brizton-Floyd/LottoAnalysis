package com.lottoanalysis.models.gameout;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DrawPosition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GroupRange extends Range {

    private static Map<AnalyzeMethod, Integer[]> analyzeMethodMap = new HashMap<>();
    private GameOutComputer gameOutComputer = new GameOutComputer();
    private DrawPosition drawPosition;
    private AnalyzeMethod analyzeMethod;
    private int[][] drawNumbers;

    private GroupRange() {

    }

    GroupRange(int range, int minNumber, int maxNumber, int[][] drawNumbers, DrawPosition drawPosition, AnalyzeMethod analyzeMethod) {
        super(range, minNumber, maxNumber);

        this.analyzeMethod = analyzeMethod;
        this.drawNumbers = drawNumbers;
        this.drawPosition = drawPosition;

        loadMap();
        checkUpperLowerBoundValues();
    }

    private void loadMap() {

        analyzeMethodMap.put(AnalyzeMethod.DRAW_POSITION, new Integer[]{getMinNumber(), getMaxNumber()});
        analyzeMethodMap.put(AnalyzeMethod.LAST_DIGIT, new Integer[]{0, 9});
        analyzeMethodMap.put(AnalyzeMethod.POSITIONAL_SUMS, new Integer[]{getMinNumber(), convertMaxToSum()});
        analyzeMethodMap.put(AnalyzeMethod.REMAINDER, new Integer[]{0, 2});
    }

    private Integer convertMaxToSum() {

        String[] splitNumber = Integer.toString(getMaxNumber()).split("");
        if (splitNumber.length > 1) {
            return Integer.parseInt(splitNumber[0]) + Integer.parseInt(splitNumber[1]);
        }

        return Integer.parseInt(splitNumber[0]);
    }

    @Override
    public void analyze() {
        computeRangeUpperLowerBound();
        computeGamesOut();
        computeHitsAtGamesOut();
        findLastOccurenceOfGameOut();
    }

    @Override
    protected void computeRangeUpperLowerBound() {

        for (int i = 0; i <= getMaxNumber(); i++) {

            GroupRange groupRange = new GroupRange();
            groupRange.setLowerBound(i);
            groupRange.setUpperBound((i + getRange()) - 1);
            groupRange.setIndex();
            getRanges().add(groupRange);

            setMinNumber(groupRange.getUpperBound());
            i = getMinNumber();
        }

        validateUpperBoundsForOverflow();
        super.resetLowerUpperBound();
    }

    List<List<String>> getLottoNumberHitDistrubutions(int index) {
        List<List<String>> data = new ArrayList<>();
        Range range = getRanges().get(index);
        Collection<List<String>> values = range.getLottoNumberMap().values();
        data.addAll(values);

        return data;
    }

    private void checkUpperLowerBoundValues() {

        Integer[] numbers = analyzeMethodMap.get(analyzeMethod);

        switch (analyzeMethod) {

            case LAST_DIGIT:
                setMinNumber(numbers[0]);
                setMaxNumber(numbers[1]);
                super.assignNewNumberToLowerUpperBound();

                break;
            case DRAW_POSITION:
                setMinNumber(numbers[0]);
                setMaxNumber(numbers[1]);
                super.assignNewNumberToLowerUpperBound();
            case POSITIONAL_SUMS:
                setMinNumber(numbers[0]);
                setMaxNumber(numbers[1]);
                super.assignNewNumberToLowerUpperBound();
            case REMAINDER:
                setMinNumber(numbers[0]);
                setMaxNumber(numbers[1]);
                super.assignNewNumberToLowerUpperBound();
                break;
        }

    }

    private void computeGamesOut() {

        gameOutComputer.loadLottoNumberGameOutList();

        for (int i = 0; i < drawNumbers[0].length; i++) {

            int[] completeDrawNumber = new int[drawNumbers.length];
            for (int j = 0; j < drawNumbers.length; j++) {
                completeDrawNumber[j] = drawNumbers[j][i];
            }

            int[] positionNumbers = drawNumbers[drawPosition.getIndex()];
            gameOutComputer.resetHitsAndFormHitPattern(completeDrawNumber, drawPosition.getIndex());
            gameOutComputer.incrementGamesOutForNonWinning(completeDrawNumber);
            List<Map<Integer, List<String>>> lottoNumberAndGamesOutList = gameOutComputer.getLottoNumbersAndGamesOut();
            placeNumberAndGameOutInMap(positionNumbers[i], lottoNumberAndGamesOutList);
            incrementHitsForAppropriateRange(positionNumbers[i]);
        }
    }

    private class GameOutComputer {

        List<GameOutComputer> gameOutComputers = new ArrayList<>();
        List<String> lottoNumberHitPattern = new ArrayList<>();
        int gamesOut, lottoNumber;

        private void loadLottoNumberGameOutList() {

            for (int i = 0; i <= getMaxNumber(); i++) {
                GameOutComputer gameOutComputer = new GameOutComputer();
                gameOutComputer.setLottoNumber(i);

                gameOutComputers.add(gameOutComputer);
            }
        }

        private List<String> getLottoNumberHitPattern() {
            return lottoNumberHitPattern;
        }

        private void setLottoNumber(int lottoNumber) {
            this.lottoNumber = lottoNumber;
        }

        public int getGamesOut() {
            return gamesOut;
        }

        List<GameOutComputer> getGameOutComputers() {
            return gameOutComputers;
        }

        private void setGamesOut(int gamesOut) {
            this.gamesOut = gamesOut;
        }

        private void incrementGamesOutForNonWinning(int[] completeDrawNumber) {

            final List<Integer> drawNumbers = Arrays.stream(completeDrawNumber).boxed().collect(Collectors.toList());

            gameOutComputers.forEach(gameOutComputer -> {

                if (!drawNumbers.contains( gameOutComputer.lottoNumber )) {
                    int out = gameOutComputer.getGamesOut();
                    ++out;
                    gameOutComputer.lottoNumberHitPattern.add(out + "");
                    gameOutComputer.setGamesOut(out);
                }
            });
        }

        private void resetHitsAndFormHitPattern(int[] completeDrawNumber, int drawIndex) {
            final List<Integer> drawNumbers = Arrays.stream(completeDrawNumber).boxed().collect(Collectors.toList());

            List<GameOutComputer> gameOutComputerList = gameOutComputers.stream().filter(gameOutComputer -> drawNumbers.contains(gameOutComputer.lottoNumber))
                    .collect(Collectors.toList());

            final Map<Integer, GameOutComputer> gameOutComputerMap = new HashMap<>();

            for (GameOutComputer gameOutComputer : gameOutComputerList) {
                //if(gameOutComputer.lottoNumber == drawNumbers.get(drawIndex))
                    gameOutComputerMap.put(gameOutComputer.lottoNumber, gameOutComputer);
            }

            boolean isPatternForAllNumbersProcessed;
            for (Integer drawNumber : drawNumbers) {
                GameOutComputer gameOutComputer = gameOutComputerMap.get(drawNumber);
                if (gameOutComputer != null) {
                    Object[] data = buildHitPattern(drawIndex, drawNumbers, gameOutComputer.lottoNumber);
                    final String pattern = (String) data[0];
                    isPatternForAllNumbersProcessed = (boolean) data[1];
                    if (isPatternForAllNumbersProcessed) {
                        gameOutComputer.getLottoNumberHitPattern().add(pattern);
                        gameOutComputer.setGamesOut(0);
                    }
                }
            }
        }

        private Object[] buildHitPattern(int drawIndex, List<Integer> drawNumbers, int lottoNumber) {

            StringBuilder stringBuilder = new StringBuilder();

            int[] hitIndexes = IntStream.range(0, drawNumbers.size()).filter(index -> drawNumbers.get(index) == lottoNumber).toArray();
            for (int i = 0; i < hitIndexes.length; i++) {
                drawNumbers.set(hitIndexes[i], -1);
            }

            for (Integer index : hitIndexes) {
                if (index == drawIndex) {
                    if (stringBuilder.toString().length() > 1)
                        stringBuilder.append("->").append("##");
                    else {
                        stringBuilder.append("##");
                    }
                }
                else {
                    final int size = stringBuilder.toString().length();
                    if (size == 0) {
                        stringBuilder.append("P").append((index + 1));
                    } else {
                        stringBuilder.append("->").append("P").append((index + 1));
                    }
                }
            }

            if (stringBuilder.toString().startsWith("->"))
                return new Object[]{"", false};

            return new Object[]{stringBuilder.toString(), true};
        }

        List<Map<Integer, List<String>>> getLottoNumbersAndGamesOut() {

            List<Map<Integer, List<String>>> mapList = new ArrayList<>();

            gameOutComputers.forEach(gameOutComputer -> {
                Map<Integer, List<String>> map = new HashMap<>();
                map.put(gameOutComputer.lottoNumber, gameOutComputer.getLottoNumberHitPattern());
                mapList.add(map);
            });

            return mapList;
        }
    }
}
