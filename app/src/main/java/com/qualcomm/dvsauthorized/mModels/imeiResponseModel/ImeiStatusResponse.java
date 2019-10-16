/*
 * Copyright (c) 2018-2019 Qualcomm Technologies, Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted (subject to the limitations in the disclaimer below) provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of Qualcomm Technologies, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment is required by displaying the trademark/log as per the details provided here: [https://www.qualcomm.com/documents/dirbs-logo-and-brand-guidelines]
 *  Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
 *  This notice may not be removed or altered from any source distribution.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.dvsauthorized.mModels.imeiResponseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// Model class for imei status response data
public class ImeiStatusResponse implements Serializable {

	@SerializedName("classification_state")
	private ClassificationState classificationState;

	@SerializedName("gsma")
	private Gsma gsma;

	@SerializedName("stolen_status")
	private String stolenStatus;

	@SerializedName("registration_status")
	private String registrationStatus;

	@SerializedName("subscribers")
	private Subscribers subscribers;

	@SerializedName("imei")
	private String imei;

	@SerializedName("compliant")
	private Compliant compliant;

	@SerializedName("pairs")
	private Pairs pairs;

	public void setClassificationState(ClassificationState classificationState){
		this.classificationState = classificationState;
	}

	public ClassificationState getClassificationState(){
		return classificationState;
	}

	public void setGsma(Gsma gsma){
		this.gsma = gsma;
	}

	public Gsma getGsma(){
		return gsma;
	}

	public void setStolenStatus(String stolenStatus){
		this.stolenStatus = stolenStatus;
	}

	public String getStolenStatus(){
		return stolenStatus;
	}

	public void setRegistrationStatus(String registrationStatus){
		this.registrationStatus = registrationStatus;
	}

	public String getRegistrationStatus(){
		return registrationStatus;
	}

	public void setSubscribers(Subscribers subscribers){
		this.subscribers = subscribers;
	}

	public Subscribers getSubscribers(){
		return subscribers;
	}

	public void setImei(String imei){
		this.imei = imei;
	}

	public String getImei(){
		return imei;
	}

	public void setCompliant(Compliant compliant){
		this.compliant = compliant;
	}

	public Compliant getCompliant(){
		return compliant;
	}

	public void setPairs(Pairs pairs){
		this.pairs = pairs;
	}

	public Pairs getPairs(){
		return pairs;
	}

	@Override
 	public String toString(){
		return 
			"ImeiStatusResponse{" + 
			"classification_state = '" + classificationState + '\'' + 
			",gsma = '" + gsma + '\'' + 
			",stolen_status = '" + stolenStatus + '\'' + 
			",registration_status = '" + registrationStatus + '\'' + 
			",subscribers = '" + subscribers + '\'' + 
			",imei = '" + imei + '\'' + 
			",compliant = '" + compliant + '\'' + 
			",pairs = '" + pairs + '\'' + 
			"}";
		}
}