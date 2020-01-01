/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.recommender.v1beta1.it;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.api.gax.rpc.PermissionDeniedException;
import com.google.cloud.ServiceOptions;
import com.google.cloud.recommender.v1beta1.GetRecommendationRequest;
import com.google.cloud.recommender.v1beta1.ListRecommendationsRequest;
import com.google.cloud.recommender.v1beta1.MarkRecommendationClaimedRequest;
import com.google.cloud.recommender.v1beta1.MarkRecommendationSucceededRequest;
import com.google.cloud.recommender.v1beta1.Recommendation;
import com.google.cloud.recommender.v1beta1.RecommendationName;
import com.google.cloud.recommender.v1beta1.RecommenderClient;
import com.google.cloud.recommender.v1beta1.RecommenderName;
import com.google.common.collect.Lists;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ITSystemTest {

  private RecommenderClient client;
  private static final String PROJECT = ServiceOptions.getDefaultProjectId();
  private static final String LOCATION = "global";
  private static final String RECOMMENDER = "google.iam.policy.Recommender";
  private static final String FORMATTED_PARENT =
      RecommenderName.of(PROJECT, LOCATION, RECOMMENDER).toString();
  private static final String RECOMMENDATION = "invalid-recommendation-name";
  private static final String ETAG = "invalid-etag";
  private static final String FORMATTED_RECOMMENDATION_NAME =
      RecommendationName.of(LOCATION, PROJECT, RECOMMENDER, RECOMMENDATION).toString();

  @Before
  public void setUp() throws Exception {
    client = RecommenderClient.create();
  }

  @After
  public void tearDown() {
    client.close();
  }

  @Test
  public void listRecommendationsTest() {
    ListRecommendationsRequest request =
        ListRecommendationsRequest.newBuilder().setParent(FORMATTED_PARENT).setFilter("").build();
    List<Recommendation> recommendations =
        Lists.newArrayList(client.listRecommendations(request).iterateAll());
    assertTrue(recommendations.size() > 0);
    assertFalse(recommendations.contains(null));
  }

  @Test(expected = InvalidArgumentException.class)
  public void listRecommendationsExceptionTest() {
    String filter = "invalid-filter";
    ListRecommendationsRequest request =
        ListRecommendationsRequest.newBuilder()
            .setParent(FORMATTED_PARENT)
            .setFilter(filter)
            .build();
    client.listRecommendations(request);
  }

  @Test(expected = PermissionDeniedException.class)
  public void getRecommendationExceptionTest() {
    GetRecommendationRequest request =
        GetRecommendationRequest.newBuilder().setName(FORMATTED_RECOMMENDATION_NAME).build();
    client.getRecommendation(request);
  }

  @Test(expected = PermissionDeniedException.class)
  public void markRecommendationClaimedExceptionTest() {
    MarkRecommendationClaimedRequest request =
        MarkRecommendationClaimedRequest.newBuilder()
            .setName(FORMATTED_RECOMMENDATION_NAME)
            .setEtag(ETAG)
            .build();
    client.markRecommendationClaimed(request);
  }

  @Test(expected = PermissionDeniedException.class)
  public void markRecommendationSucceededExceptionTest() {
    MarkRecommendationSucceededRequest request =
        MarkRecommendationSucceededRequest.newBuilder()
            .setName(FORMATTED_RECOMMENDATION_NAME)
            .setEtag(ETAG)
            .build();
    client.markRecommendationSucceeded(request);
  }
}
