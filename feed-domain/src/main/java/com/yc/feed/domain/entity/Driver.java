package com.yc.feed.domain.entity;

/*
*司机信息
*/
public class Driver {
    private Integer driverId;

    private Integer companyId;

    private Integer labourCompanyId;

    private String name;

    private String loginName;

    private String password;

    private String identityCard;

    private String driveLicense;

    private String cellphone;

    private String email;

    private String gender;

    private Byte age;

    private Integer licenseStartDate;

    private Integer licenseEndDate;

    private String img;

    private Integer carId;

    private Integer quasiCarId;

    private Integer lastLoginTime;

    private Integer lastLogoutTime;

    private Integer rate;

    private String city;

    private Integer telCode;

    private Long deviceId;

    private Integer auditStatus;

    private String licenseType;

    private String licenseImg;

    private Byte workStatus;

    private Byte workStatusTmp;

    private Integer shizhuPriceValue;

    private Integer fpPriceValue;

    private Integer contribution;

    private Integer contributionExt;

    private Integer score;

    private Integer createTime;

    private Integer firstConfirmTime;

    private Boolean infoSource;

    private Integer trainingTime;

    private String preAuditFaultReason;

    private String permanentAddress;

    private String phoneModel;

    private String bank;

    private String bankCard;

    private String auditFaultReason;

    private Integer basePriceUpdateTime;

    private Integer airportPriceUpdateTime;

    private Integer signAt;

    private Integer expiredAt;

    private Integer acceptCount;

    private Integer silenceEndAt;

    private Integer evaluation;

    private Boolean hasQualified;

    private Integer lastAnnualAt;

    private Integer annualScore;

    private Integer nextAnnualStart;

    private Integer nextAnnualEnd;

    private String manager;

    private Integer baseScore;

    private Integer recentAcceptedCount;

    private Integer unittimeCompleteCount;

    private Boolean hasLogistics;

    private String selfintroduction;

    private String carSetup;

    private Integer agentOperatorId;

    private Integer agentId;

    private Byte isSelfEmployed;

    private String sellingPoint;

    private Integer checkScore;

    private String areaCode;

    private String country;

    private String passport;

    private Byte surpportFacePay;

    private Integer goodCommentCount;

    private Integer badCommentCount;

    private Integer goodCommentRate;

    private Byte isBalanceBad;

    private Integer serviceManagerId;

    private Integer flag;

    private Long accountId;

    private String carFacilityTagId;

    private Integer completeCount;

    private String bankCode;

    private Integer driverType;

    private Integer avatarStatus;

    private Integer status;

    private Integer levelupStatus;

    private Integer dispatchType;

    private Integer discountRate;

    private Byte driverLevel;

    private Integer driverScore;

    private Byte serviceScore;

    private Byte life;

    private Integer examinationScore;

    private Byte isverifyCar;

    private Byte isverify;

    private String comment;

    private byte[] photoId;

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", companyId=" + companyId +
                ", labourCompanyId=" + labourCompanyId +
                ", name='" + name + '\'' +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", identityCard='" + identityCard + '\'' +
                ", driveLicense='" + driveLicense + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", licenseStartDate=" + licenseStartDate +
                ", licenseEndDate=" + licenseEndDate +
                ", img='" + img + '\'' +
                ", carId=" + carId +
                ", quasiCarId=" + quasiCarId +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLogoutTime=" + lastLogoutTime +
                ", rate=" + rate +
                ", city='" + city + '\'' +
                ", telCode=" + telCode +
                ", deviceId=" + deviceId +
                ", auditStatus=" + auditStatus +
                ", licenseType='" + licenseType + '\'' +
                ", licenseImg='" + licenseImg + '\'' +
                ", workStatus=" + workStatus +
                ", workStatusTmp=" + workStatusTmp +
                ", shizhuPriceValue=" + shizhuPriceValue +
                ", fpPriceValue=" + fpPriceValue +
                ", contribution=" + contribution +
                ", contributionExt=" + contributionExt +
                ", score=" + score +
                ", createTime=" + createTime +
                ", firstConfirmTime=" + firstConfirmTime +
                ", infoSource=" + infoSource +
                ", trainingTime=" + trainingTime +
                ", preAuditFaultReason='" + preAuditFaultReason + '\'' +
                ", permanentAddress='" + permanentAddress + '\'' +
                ", phoneModel='" + phoneModel + '\'' +
                ", bank='" + bank + '\'' +
                ", bankCard='" + bankCard + '\'' +
                ", auditFaultReason='" + auditFaultReason + '\'' +
                ", basePriceUpdateTime=" + basePriceUpdateTime +
                ", airportPriceUpdateTime=" + airportPriceUpdateTime +
                ", signAt=" + signAt +
                ", expiredAt=" + expiredAt +
                ", acceptCount=" + acceptCount +
                ", silenceEndAt=" + silenceEndAt +
                ", evaluation=" + evaluation +
                ", hasQualified=" + hasQualified +
                ", lastAnnualAt=" + lastAnnualAt +
                ", annualScore=" + annualScore +
                ", nextAnnualStart=" + nextAnnualStart +
                ", nextAnnualEnd=" + nextAnnualEnd +
                ", manager='" + manager + '\'' +
                ", baseScore=" + baseScore +
                ", recentAcceptedCount=" + recentAcceptedCount +
                ", unittimeCompleteCount=" + unittimeCompleteCount +
                ", hasLogistics=" + hasLogistics +
                ", selfintroduction='" + selfintroduction + '\'' +
                ", carSetup='" + carSetup + '\'' +
                ", agentOperatorId=" + agentOperatorId +
                ", agentId=" + agentId +
                ", isSelfEmployed=" + isSelfEmployed +
                ", sellingPoint='" + sellingPoint + '\'' +
                ", checkScore=" + checkScore +
                ", areaCode='" + areaCode + '\'' +
                ", country='" + country + '\'' +
                ", passport='" + passport + '\'' +
                ", surpportFacePay=" + surpportFacePay +
                ", goodCommentCount=" + goodCommentCount +
                ", badCommentCount=" + badCommentCount +
                ", goodCommentRate=" + goodCommentRate +
                ", isBalanceBad=" + isBalanceBad +
                ", serviceManagerId=" + serviceManagerId +
                ", flag=" + flag +
                '}';
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getLabourCompanyId() {
        return labourCompanyId;
    }

    public void setLabourCompanyId(Integer labourCompanyId) {
        this.labourCompanyId = labourCompanyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard == null ? null : identityCard.trim();
    }

    public String getDriveLicense() {
        return driveLicense;
    }

    public void setDriveLicense(String driveLicense) {
        this.driveLicense = driveLicense == null ? null : driveLicense.trim();
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone == null ? null : cellphone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public Integer getLicenseStartDate() {
        return licenseStartDate;
    }

    public void setLicenseStartDate(Integer licenseStartDate) {
        this.licenseStartDate = licenseStartDate;
    }

    public Integer getLicenseEndDate() {
        return licenseEndDate;
    }

    public void setLicenseEndDate(Integer licenseEndDate) {
        this.licenseEndDate = licenseEndDate;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img == null ? null : img.trim();
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Integer getQuasiCarId() {
        return quasiCarId;
    }

    public void setQuasiCarId(Integer quasiCarId) {
        this.quasiCarId = quasiCarId;
    }

    public Integer getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Integer lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Integer lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getTelCode() {
        return telCode;
    }

    public void setTelCode(Integer telCode) {
        this.telCode = telCode;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType == null ? null : licenseType.trim();
    }

    public String getLicenseImg() {
        return licenseImg;
    }

    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg == null ? null : licenseImg.trim();
    }

    public Byte getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Byte workStatus) {
        this.workStatus = workStatus;
    }

    public Byte getWorkStatusTmp() {
        return workStatusTmp;
    }

    public void setWorkStatusTmp(Byte workStatusTmp) {
        this.workStatusTmp = workStatusTmp;
    }

    public Integer getShizhuPriceValue() {
        return shizhuPriceValue;
    }

    public void setShizhuPriceValue(Integer shizhuPriceValue) {
        this.shizhuPriceValue = shizhuPriceValue;
    }

    public Integer getFpPriceValue() {
        return fpPriceValue;
    }

    public void setFpPriceValue(Integer fpPriceValue) {
        this.fpPriceValue = fpPriceValue;
    }

    public Integer getContribution() {
        return contribution;
    }

    public void setContribution(Integer contribution) {
        this.contribution = contribution;
    }

    public Integer getContributionExt() {
        return contributionExt;
    }

    public void setContributionExt(Integer contributionExt) {
        this.contributionExt = contributionExt;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getFirstConfirmTime() {
        return firstConfirmTime;
    }

    public void setFirstConfirmTime(Integer firstConfirmTime) {
        this.firstConfirmTime = firstConfirmTime;
    }

    public Boolean getInfoSource() {
        return infoSource;
    }

    public void setInfoSource(Boolean infoSource) {
        this.infoSource = infoSource;
    }

    public Integer getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(Integer trainingTime) {
        this.trainingTime = trainingTime;
    }

    public String getPreAuditFaultReason() {
        return preAuditFaultReason;
    }

    public void setPreAuditFaultReason(String preAuditFaultReason) {
        this.preAuditFaultReason = preAuditFaultReason == null ? null : preAuditFaultReason.trim();
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress == null ? null : permanentAddress.trim();
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel == null ? null : phoneModel.trim();
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank == null ? null : bank.trim();
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard == null ? null : bankCard.trim();
    }

    public String getAuditFaultReason() {
        return auditFaultReason;
    }

    public void setAuditFaultReason(String auditFaultReason) {
        this.auditFaultReason = auditFaultReason == null ? null : auditFaultReason.trim();
    }

    public Integer getBasePriceUpdateTime() {
        return basePriceUpdateTime;
    }

    public void setBasePriceUpdateTime(Integer basePriceUpdateTime) {
        this.basePriceUpdateTime = basePriceUpdateTime;
    }

    public Integer getAirportPriceUpdateTime() {
        return airportPriceUpdateTime;
    }

    public void setAirportPriceUpdateTime(Integer airportPriceUpdateTime) {
        this.airportPriceUpdateTime = airportPriceUpdateTime;
    }

    public Integer getSignAt() {
        return signAt;
    }

    public void setSignAt(Integer signAt) {
        this.signAt = signAt;
    }

    public Integer getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Integer expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Integer getAcceptCount() {
        return acceptCount;
    }

    public void setAcceptCount(Integer acceptCount) {
        this.acceptCount = acceptCount;
    }

    public Integer getSilenceEndAt() {
        return silenceEndAt;
    }

    public void setSilenceEndAt(Integer silenceEndAt) {
        this.silenceEndAt = silenceEndAt;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public Boolean getHasQualified() {
        return hasQualified;
    }

    public void setHasQualified(Boolean hasQualified) {
        this.hasQualified = hasQualified;
    }

    public Integer getLastAnnualAt() {
        return lastAnnualAt;
    }

    public void setLastAnnualAt(Integer lastAnnualAt) {
        this.lastAnnualAt = lastAnnualAt;
    }

    public Integer getAnnualScore() {
        return annualScore;
    }

    public void setAnnualScore(Integer annualScore) {
        this.annualScore = annualScore;
    }

    public Integer getNextAnnualStart() {
        return nextAnnualStart;
    }

    public void setNextAnnualStart(Integer nextAnnualStart) {
        this.nextAnnualStart = nextAnnualStart;
    }

    public Integer getNextAnnualEnd() {
        return nextAnnualEnd;
    }

    public void setNextAnnualEnd(Integer nextAnnualEnd) {
        this.nextAnnualEnd = nextAnnualEnd;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager == null ? null : manager.trim();
    }

    public Integer getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(Integer baseScore) {
        this.baseScore = baseScore;
    }

    public Integer getRecentAcceptedCount() {
        return recentAcceptedCount;
    }

    public void setRecentAcceptedCount(Integer recentAcceptedCount) {
        this.recentAcceptedCount = recentAcceptedCount;
    }

    public Integer getUnittimeCompleteCount() {
        return unittimeCompleteCount;
    }

    public void setUnittimeCompleteCount(Integer unittimeCompleteCount) {
        this.unittimeCompleteCount = unittimeCompleteCount;
    }

    public Boolean getHasLogistics() {
        return hasLogistics;
    }

    public void setHasLogistics(Boolean hasLogistics) {
        this.hasLogistics = hasLogistics;
    }

    public String getSelfintroduction() {
        return selfintroduction;
    }

    public void setSelfintroduction(String selfintroduction) {
        this.selfintroduction = selfintroduction == null ? null : selfintroduction.trim();
    }

    public String getCarSetup() {
        return carSetup;
    }

    public void setCarSetup(String carSetup) {
        this.carSetup = carSetup == null ? null : carSetup.trim();
    }

    public Integer getAgentOperatorId() {
        return agentOperatorId;
    }

    public void setAgentOperatorId(Integer agentOperatorId) {
        this.agentOperatorId = agentOperatorId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Byte getIsSelfEmployed() {
        return isSelfEmployed;
    }

    public void setIsSelfEmployed(Byte isSelfEmployed) {
        this.isSelfEmployed = isSelfEmployed;
    }

    public String getSellingPoint() {
        return sellingPoint;
    }

    public void setSellingPoint(String sellingPoint) {
        this.sellingPoint = sellingPoint == null ? null : sellingPoint.trim();
    }

    public Integer getCheckScore() {
        return checkScore;
    }

    public void setCheckScore(Integer checkScore) {
        this.checkScore = checkScore;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport == null ? null : passport.trim();
    }

    public Byte getSurpportFacePay() {
        return surpportFacePay;
    }

    public void setSurpportFacePay(Byte surpportFacePay) {
        this.surpportFacePay = surpportFacePay;
    }

    public Integer getGoodCommentCount() {
        return goodCommentCount;
    }

    public void setGoodCommentCount(Integer goodCommentCount) {
        this.goodCommentCount = goodCommentCount;
    }

    public Integer getBadCommentCount() {
        return badCommentCount;
    }

    public void setBadCommentCount(Integer badCommentCount) {
        this.badCommentCount = badCommentCount;
    }

    public Integer getGoodCommentRate() {
        return goodCommentRate;
    }

    public void setGoodCommentRate(Integer goodCommentRate) {
        this.goodCommentRate = goodCommentRate;
    }

    public Byte getIsBalanceBad() {
        return isBalanceBad;
    }

    public void setIsBalanceBad(Byte isBalanceBad) {
        this.isBalanceBad = isBalanceBad;
    }

    public Integer getServiceManagerId() {
        return serviceManagerId;
    }

    public void setServiceManagerId(Integer serviceManagerId) {
        this.serviceManagerId = serviceManagerId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCarFacilityTagId() {
        return carFacilityTagId;
    }

    public void setCarFacilityTagId(String carFacilityTagId) {
        this.carFacilityTagId = carFacilityTagId == null ? null : carFacilityTagId.trim();
    }

    public Integer getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(Integer completeCount) {
        this.completeCount = completeCount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public Integer getDriverType() {
        return driverType;
    }

    public void setDriverType(Integer driverType) {
        this.driverType = driverType;
    }

    public Integer getAvatarStatus() {
        return avatarStatus;
    }

    public void setAvatarStatus(Integer avatarStatus) {
        this.avatarStatus = avatarStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLevelupStatus() {
        return levelupStatus;
    }

    public void setLevelupStatus(Integer levelupStatus) {
        this.levelupStatus = levelupStatus;
    }

    public Integer getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(Integer dispatchType) {
        this.dispatchType = dispatchType;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }

    public Byte getDriverLevel() {
        return driverLevel;
    }

    public void setDriverLevel(Byte driverLevel) {
        this.driverLevel = driverLevel;
    }

    public Integer getDriverScore() {
        return driverScore;
    }

    public void setDriverScore(Integer driverScore) {
        this.driverScore = driverScore;
    }

    public Byte getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Byte serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Byte getLife() {
        return life;
    }

    public void setLife(Byte life) {
        this.life = life;
    }

    public Integer getExaminationScore() {
        return examinationScore;
    }

    public void setExaminationScore(Integer examinationScore) {
        this.examinationScore = examinationScore;
    }

    public Byte getIsverifyCar() {
        return isverifyCar;
    }

    public void setIsverifyCar(Byte isverifyCar) {
        this.isverifyCar = isverifyCar;
    }

    public Byte getIsverify() {
        return isverify;
    }

    public void setIsverify(Byte isverify) {
        this.isverify = isverify;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public byte[] getPhotoId() {
        return photoId;
    }

    public void setPhotoId(byte[] photoId) {
        this.photoId = photoId;
    }
}