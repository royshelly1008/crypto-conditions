package com.ripple.cryptoconditions.jackson;

/*-
 * ========================LICENSE_START=================================
 * Crypto-Conditions Jackson
 * %%
 * Copyright (C) 2018 Ripple Labs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.ripple.cryptoconditions.PreimageSha256Fulfillment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Validates the functionality of {@link CryptoConditionsModule}.
 */
@RunWith(Parameterized.class)
public class PreimageSha256FulfillmentCryptoConditionModuleTest extends
    AbstractCryptoConditionsModuleTest {

  private static final String PREIMAGE_FULFILLMENT_DER_BYTES_HEX =
      "A02D802B796F75206275696C7420612074696D65206D616368696E65206F7574206F6620612044654C6F7265616E3F";
  private static final String PREIMAGE_FULFILLMENT_DER_BYTES_BASE64
      = "oC2AK3lvdSBidWlsdCBhIHRpbWUgbWFjaGluZSBvdXQgb2YgYSBEZUxvcmVhbj8=";
  private static final String PREIMAGE_FULFILLMENT_DER_BYTES_BASE64_WITHOUTPADDING
      = "oC2AK3lvdSBidWlsdCBhIHRpbWUgbWFjaGluZSBvdXQgb2YgYSBEZUxvcmVhbj8";
  private static final String PREIMAGE_FULFILLMENT_DER_BYTES_BASE64_URL
      = "oC2AK3lvdSBidWlsdCBhIHRpbWUgbWFjaGluZSBvdXQgb2YgYSBEZUxvcmVhbj8=";
  private static final String PREIMAGE_FULFILLMENT_DER_BYTES_BASE64_URL_WITHOUTPADDING
      = "oC2AK3lvdSBidWlsdCBhIHRpbWUgbWFjaGluZSBvdXQgb2YgYSBEZUxvcmVhbj8";

  private static PreimageSha256Fulfillment FULFILLMENT = constructPreimageFulfillment();

  /**
   * Required-args Constructor (used by JUnit's parameterized test annotation).
   *
   * @param encodingToUse        A {@link Encoding} to use for each test run.
   * @param expectedEncodedValue A {@link String} encoded in the above encoding to assert against.
   */
  public PreimageSha256FulfillmentCryptoConditionModuleTest(
      final Encoding encodingToUse, final String expectedEncodedValue
  ) {
    super(encodingToUse, expectedEncodedValue);
  }

  /**
   * Get test parameters.
   * @return the parameters for the tests
   */
  @Parameters
  public static Collection<Object[]> data() {
    // Create and return a Collection of Object arrays. Each element in each array is a parameter
    // to the CryptoConditionsModuleFulfillmentTest constructor.
    return Arrays.asList(new Object[][]{
        {Encoding.HEX, PREIMAGE_FULFILLMENT_DER_BYTES_HEX},
        {Encoding.BASE64, PREIMAGE_FULFILLMENT_DER_BYTES_BASE64},
        {Encoding.BASE64_WITHOUT_PADDING, PREIMAGE_FULFILLMENT_DER_BYTES_BASE64_WITHOUTPADDING},
        {Encoding.BASE64URL, PREIMAGE_FULFILLMENT_DER_BYTES_BASE64_URL},
        {Encoding.BASE64URL_WITHOUT_PADDING, PREIMAGE_FULFILLMENT_DER_BYTES_BASE64_URL_WITHOUTPADDING},
    });

  }

  @Test
  public void testSerializeDeserialize() throws IOException {
    final PreimageFulfillmentContainer expectedContainer = ImmutablePreimageFulfillmentContainer
        .builder()
        .fulfillment(FULFILLMENT)
        .build();

    final String json = objectMapper.writeValueAsString(expectedContainer);
    assertThat(json, is(
        String.format("{\"fulfillment\":\"%s\"}", expectedEncodedValue)
    ));

    final PreimageFulfillmentContainer actualAddressContainer = objectMapper
        .readValue(json, PreimageFulfillmentContainer.class);

    assertThat(actualAddressContainer, is(expectedContainer));
    assertThat(actualAddressContainer.getFulfillment(), is(FULFILLMENT));
  }

  @Value.Immutable
  @JsonSerialize(as = ImmutablePreimageFulfillmentContainer.class)
  @JsonDeserialize(as = ImmutablePreimageFulfillmentContainer.class)
  interface PreimageFulfillmentContainer {

    @JsonProperty("fulfillment")
    PreimageSha256Fulfillment getFulfillment();
  }

}
