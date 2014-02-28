package jredis;

/**
 * An Element to capture the member and its score. a.k.a the zelement.
 * 
 * @author anoopelias
 *
 */
public class Element implements Comparable<Element> {

    private Double score;
    private String member;

    /**
     * Create an element with its member and score.
     * 
     * @param member
     * @param score
     */
    public Element(String member, Double score) {
        this.member = member;
        this.score = score;
    }

    /**
     * Get the score of the element.
     * 
     * @return
     */
    public Double getScore() {
        return score;
    }

    /**
     * Get the member string of the element.
     * 
     * @return
     */
    public String getMember() {
        return member;
    }

    @Override
    public int hashCode() {
        return member.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Element other = (Element) obj;
        if (member == null) {
            if (other.member != null)
                return false;
        } else if (!member.equals(other.member))
            return false;
        return true;
    }

    @Override
    public int compareTo(Element o) {
        int scoreComparison = this.score.compareTo(o.score);

        if (this.member == null || o.member == null)
            return scoreComparison;

        if (this.member.equals(o.member))
            return 0;

        if (scoreComparison == 0)
            return this.member.compareTo(o.member);

        return scoreComparison;
    }

}
