package com.gigigo.orchextra.wrapper;

import java.util.GregorianCalendar;

/**
 * Created by alex on 01/12/2017.
 */

public class CrmUser {

  private final String crmId;
  private final GregorianCalendar birthdate;
  private final Gender gender;

  /**
   * Creates an orchextra user, this user will be useful for segmentation purposes and statistic
   * tracking in dashboard
   *
   * @param crmId     CrmUser ID, can be the user name of your app
   * @param birthdate user's birth date.
   * @param gender    user's male, using an enum
   */
  public CrmUser(String crmId, GregorianCalendar birthdate, Gender gender) {
    this.crmId = crmId;
    this.birthdate = birthdate;
    this.gender = gender;
  }

  public String getCrmId() {
    return crmId;
  }

  public GregorianCalendar getBirthdate() {
    return birthdate;
  }

  public Gender getGender() {
    return gender;
  }

  //todo REFACTOR this must be a GenderType and must to encapsulate el value of this field in server, GenderMale-->"male"
  //CrmUserGenderConverter must be inside GenderType, i'm think
  public enum Gender {
    GenderMale,
    GenderFemale,
    GenderND

  }
}
