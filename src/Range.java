import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Range {
    Boolean isMapped;
    Long start;
    Long end;

    Range(Long start, Long end) {
        this.start = start;
        this.end = end;
        isMapped = false;
    }


    public static List<Range> mapRanges(List<Range> ranges, List<Range> sourceRanges, List<Range> destinationRanges) {
        List<Range> subRanges = splitRanges(ranges, sourceRanges);
        System.out.println("SubRanges: " + subRanges);
        for (Range subRange : subRanges) {
            if(subRange.isMapped) continue;
            for (int i = 0; i < sourceRanges.size(); i++) {
                Range sourceRange = sourceRanges.get(i);
                Range destinationRange = destinationRanges.get(i);
                if(!subRange.isZeroOverlap(sourceRange)) {
                    Long offset = subRange.start - sourceRange.start;
                    subRange.start = destinationRange.start + offset;
                    subRange.end = destinationRange.end - (sourceRange.end - subRange.end);
                    subRange.isMapped = true;
                    break;
                }
            }
        }
        subRanges.forEach(e -> e.isMapped = false);
        return subRanges;
    }
    private static List<Range> splitRanges(List<Range> ranges, List<Range> sourceRanges) {
        ArrayList<Range> subRanges = new ArrayList<>();
        for(Range range : ranges) {
            subRanges.addAll(splitRange(sourceRanges, range));
        }
        return subRanges;
    }
    private static List<Range> splitRange(List<Range> sourceRanges, Range range) {
        Set<Long> boundaryPoints = new TreeSet<>();
        boundaryPoints.add(range.start);
        boundaryPoints.add(range.end);

        for (Range sourceRange : sourceRanges) {
            boundaryPoints.add(sourceRange.start);
            boundaryPoints.add(sourceRange.end);
        }
        boundaryPoints.removeIf(point -> point < range.start || point > range.end);

        List<Range> subRanges = new ArrayList<>();
        Long previousPoint = null;

        for (Long point : boundaryPoints) {
            if (previousPoint != null) {
                subRanges.add(new Range(previousPoint, point));
            }
            previousPoint = point;
        }

        return subRanges;
    }



    public boolean isZeroOverlap(Range other) {
        if(this.start < other.start)
            return this.end < other.end;
        else if (this.start > other.start)
            return this.end > other.end;
        return false;
    }

    @Override
    public String toString() {
        return this.start + " " + this.end;
    }
}
