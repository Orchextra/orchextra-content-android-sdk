package com.gigigo.orchextra.wrapper;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import java.util.GregorianCalendar;


public class CrmUser {

  @NonNull private final String crmId;
  @Nullable
  private final GregorianCalendar birthdate;
  @Nullable private final Gender gender;

  /**
   * Creates an orchextra user, this user will be useful for segmentation purposes and statistic
   * tracking in dashboard
   *
   * @param crmId CrmUser ID, can be the user name of your app
   * @param birthdate user's birth date.
   * @param gender user's male, using an enum
   */
  public CrmUser(@NonNull String crmId, @Nullable GregorianCalendar birthdate,
      @Nullable Gender gender) {
    this.crmId = crmId;
    this.birthdate = birthdate;
    this.gender = gender;
  }

  @NonNull public String getCrmId() {
    return crmId;
  }

  @Nullable public GregorianCalendar getBirthdate() {
    return birthdate;
  }

  @Nullable public Gender getGender() {
    return gender;
  }

  //todo REFACTOR this must be a GenderType and must to encapsulate el value of this field in server, GenderMale-->"male"
  //CrmUserGenderConverter must be inside GenderType, i'm think
  public enum Gender {
    GenderMale, GenderFemale, GenderND

  }
}