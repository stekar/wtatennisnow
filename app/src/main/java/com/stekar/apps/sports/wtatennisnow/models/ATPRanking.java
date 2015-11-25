package com.stekar.apps.sports.wtatennisnow.models;

// DO NOT RENAME the instance member fields
// as they are used for the implicit JSON deserialization
// i.e. direct match, field-by-field

public class ATPRanking {
    private int playerId;
    private String playerFirstName;
    private String playerLastName;
    private int playerTotalPoints;
    private String playerWebSite;
    private String playerPhotoUrl;
    private String playerProfileUrl;
    private int playerTournas;
    private String playerPrizeMoneyCurrent;
    private String playerPrizeMoneyTotal;
    private int playerAge;
    private String playerBirthplace;
    private int playerRank;
    private int playerRankHigh;
    private int playerTitlesYTD;
    private int playerTitlesCareer;
    private int playerSlamTitlesYTD;
    private int playerSlamTitlesCareer;
    private int playerMasters1000TitlesYTD;
    private int playerMasters1000TitlesCareer;
    private boolean mIsStarred;
    private boolean mIsShared;
    private String mShareText;

    private final static String DELIMETER = " ";

    public ATPRanking() {
    }

    @Override
    public String toString() {
        return playerFirstName + DELIMETER + playerLastName + DELIMETER +
                playerTotalPoints + DELIMETER + DELIMETER  +
                playerTournas + DELIMETER + playerPrizeMoneyCurrent + DELIMETER + playerPrizeMoneyTotal +
                DELIMETER + playerAge + DELIMETER + playerBirthplace;
    }

    /**
     * @return the playerFirstName
     */
    public String getPlayerFirstName() {
        return playerFirstName;
    }

    /**
     * @return the playerLastName
     */
    public String getPlayerLastName() {
        return playerLastName;
    }

    /**
     * @return the playerTotalPoints
     */
    public int getPlayerTotalPoints() {
        return playerTotalPoints;
    }

    /**
     * @return the playerWebSite
     */
    public String getPlayerWebSite() {
        return playerWebSite;
    }

    /**
     * @return the playerPhoto
     */
    public String getPlayerPhotoUrl() {
        return playerPhotoUrl;
    }

    /**
     * @param firstName the title to set
     */
    public void setPlayerFirstName(String firstName) {
        this.playerFirstName = firstName;
    }

    /**
     * @param lastName the title to set
     */
    public void setPlayerLastName(String lastName) {
        this.playerLastName = lastName;
    }

    /**
     * @param totalPoints the title to set
     */
    public void setPlayerTotalPoints(int totalPoints) {
        this.playerTotalPoints = totalPoints;
    }


    /**
     * @param playerWebSite the title to set
     */
    public void setPlayerWebSite(String playerWebSite) {
        this.playerWebSite = playerWebSite;
    }

    /**
     * @param playerPhotoUrl the title to set
     */
    public void setPlayerPhotoUrl(String playerPhotoUrl) {
        this.playerPhotoUrl = playerPhotoUrl;
    }

    public String getPlayerProfileUrl() {
        return playerProfileUrl;
    }

    public void setPlayerProfileUrl(String playerProfile) {
        this.playerProfileUrl = playerProfile;
    }

    public int getPlayerTournas() {
        return playerTournas;
    }

    public void setPlayerTournas(int playerTournas) {
        this.playerTournas = playerTournas;
    }

    public String getPlayerPrizeMoneyCurrent() {
        return playerPrizeMoneyCurrent;
    }

    public void setPlayerPrizeMoneyCurrent(String playerPrizeMoneyCurrent) {
        this.playerPrizeMoneyCurrent = playerPrizeMoneyCurrent;
    }

    public String getPlayerPrizeMoneyTotal() {
        return playerPrizeMoneyTotal;
    }

    public void setPlayerPrizeMoneyTotal(String playerPrizeMoneyTotal) {
        this.playerPrizeMoneyTotal = playerPrizeMoneyTotal;
    }

    public int getPlayerAge() {
        return playerAge;
    }

    public void setPlayerAge(int playerAge) {
        this.playerAge = playerAge;
    }

    public String getPlayerBirthplace() {
        return playerBirthplace;
    }

    public void setPlayerBirthplace(String playerBirthplace) {
        this.playerBirthplace = playerBirthplace;
    }

    public int getPlayerRank() {
        return playerRank;
    }

    public void setPlayerRank(int playerRank) {
        this.playerRank = playerRank;
    }

    public int getPlayerRankHigh() {
        return playerRankHigh;
    }

    public void setPlayerRankHigh(int playerRankHigh) {
        this.playerRankHigh = playerRankHigh;
    }


    public int getPlayerTitlesYTD() {
        return playerTitlesYTD;
    }

    public void setPlayerTitlesYTD(int playerTitlesYTD) {
        this.playerTitlesYTD = playerTitlesYTD;
    }

    public int getPlayerTitlesCareer() {
        return playerTitlesCareer;
    }

    public void setPlayerTitlesCareer(int playerTitlesCareer) {
        this.playerTitlesCareer = playerTitlesCareer;
    }

    public int getPlayerSlamTitlesYTD() {
        return playerSlamTitlesYTD;
    }

    public void setPlayerSlamTitlesYTD(int playerSlamTitlesYTD) {
        this.playerSlamTitlesYTD = playerSlamTitlesYTD;
    }

    public int getPlayerSlamTitlesCareer() {
        return playerSlamTitlesCareer;
    }

    public void setPlayerSlamTitlesCareer(int playerSlamTitlesCareer) {
        this.playerSlamTitlesCareer = playerSlamTitlesCareer;
    }

    public int getPlayerMasters1000TitlesYTD() {
        return playerMasters1000TitlesYTD;
    }

    public void setPlayerMasters1000Titles(int playerMasters1000TitlesYTD) {
        this.playerMasters1000TitlesYTD = playerMasters1000TitlesYTD;
    }

    public int getPlayerMasters1000TitlesCareer() {
        return playerMasters1000TitlesCareer;
    }

    public void setPlayerMasters1000TitlesCareer(int playerMasters1000TitlesCareer) {
        this.playerMasters1000TitlesCareer = playerMasters1000TitlesCareer;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean getIsStarred() { return mIsStarred; }
    public void setIsStarred(boolean flag) { mIsStarred = flag; }

    public boolean getIsShared() { return mIsShared; }
    public void setIsShared(boolean flag) { mIsShared = flag; }

    public String getShareText() {
        return mShareText;
    }

    public void setShareText(String shareText) {
        mShareText = shareText;
    }
}
