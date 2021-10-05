/*
 * Copyright 2021 Open Raven Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.openraven.magpie.core.cspm.analysis;

import io.openraven.magpie.core.cspm.model.Policy;
import io.openraven.magpie.core.cspm.model.Rule;

import java.time.LocalDateTime;

public class Violation {
  private Policy policy;
  private Rule rule;
  private String assetId;
  private String info;
  private String error;
  private LocalDateTime evaluatedAt;

  public Violation() {
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public LocalDateTime getEvaluatedAt() {
    return evaluatedAt;
  }

  public void setEvaluatedAt(LocalDateTime evaluatedAt) {
    this.evaluatedAt = evaluatedAt;
  }

  public Policy getPolicy() {
    return policy;
  }

  public void setPolicy(Policy policy) {
    this.policy = policy;
  }

  public Rule getRule() {
    return rule;
  }

  public void setRule(Rule rule) {
    this.rule = rule;
  }

  public String getAssetId() {
    return assetId;
  }

  public void setAssetId(String assetId) {
    this.assetId = assetId;
  }

  @Override
  public String toString() {
    return "Violation{" +
      "policy=" + policy.getPolicyId() +
      ", rule=" + rule.getRuleId() +
      ", assetId='" + assetId + '\'' +
      ", info='" + info + '\'' +
      ", error='" + error + '\'' +
      ", evaluatedAt=" + evaluatedAt +
      '}';
  }
}
