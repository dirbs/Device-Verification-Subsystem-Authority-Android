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

package com.qualcom.dvsauthorized.mTestUtilities;

// Response strings for imei status
public class ImeiStatusResponses {

    public static final String successResponse = "{\n" +
            "    \"registration_status\": \"Registered\",\n" +
            "    \"stolen_status\": \"No report\",\n" +
            "    \"pairs\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-12-31\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"imei\": \"12345678912345\",\n" +
            "    \"classification_state\": {\n" +
            "        \"blocking_conditions\": [],\n" +
            "        \"informative_conditions\": [\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"duplicate_compound\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"gsma\": {\n" +
            "        \"brand\": \"Not Known\",\n" +
            "        \"manufacturer\": \"BlackBerry Limited\",\n" +
            "        \"model_name\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"device_type\": \"Handheld\",\n" +
            "        \"radio_access_technology\": \"GSM 1800,GSM 900\",\n" +
            "        \"model_number\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"operating_system\": null\n" +
            "    },\n" +
            "    \"subscribers\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-12-31\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"compliant\": {\n" +
            "        \"status\": \"Compliant (Active)\"\n" +
            "    }\n" +
            "}";

    public static final String loadMoreSubResponse = "{\n" +
            "    \"registration_status\": \"Registered\",\n" +
            "    \"stolen_status\": \"No report\",\n" +
            "    \"pairs\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-12-31\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"imei\": \"12345678912345\",\n" +
            "    \"classification_state\": {\n" +
            "        \"blocking_conditions\": [\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"gsma_not_found\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"on_local_stolen_list\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"not_on_registration_list\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"informative_conditions\": [\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"duplicate_compound\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"gsma\": {\n" +
            "        \"brand\": \"Not Known\",\n" +
            "        \"manufacturer\": \"BlackBerry Limited\",\n" +
            "        \"model_name\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"device_type\": \"Handheld\",\n" +
            "        \"radio_access_technology\": \"GSM 1800,GSM 900\",\n" +
            "        \"model_number\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"operating_system\": null\n" +
            "    },\n" +
            "    \"subscribers\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"imsi\": \"112233445566778899\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"compliant\": {\n" +
            "        \"status\": \"Compliant (Active)\"\n" +
            "    }\n" +
            "}";

    public static final String loadMoreSubNoData = "{\n" +
            "    \"registration_status\": \"Registered\",\n" +
            "    \"stolen_status\": \"No report\",\n" +
            "    \"pairs\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"2017-12-31\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"imei\": \"12345678912345\",\n" +
            "    \"classification_state\": {\n" +
            "        \"blocking_conditions\": [\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"gsma_not_found\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"on_local_stolen_list\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"not_on_registration_list\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"informative_conditions\": [\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"duplicate_compound\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"gsma\": {\n" +
            "        \"brand\": \"Not Known\",\n" +
            "        \"manufacturer\": \"BlackBerry Limited\",\n" +
            "        \"model_name\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"device_type\": \"Handheld\",\n" +
            "        \"radio_access_technology\": \"GSM 1800,GSM 900\",\n" +
            "        \"model_number\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"operating_system\": null\n" +
            "    },\n" +
            "    \"subscribers\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"compliant\": {\n" +
            "        \"status\": \"Compliant (Active)\"\n" +
            "    }\n" +
            "}";

    public static final String loadMorePairedResponse = "{\n" +
            "    \"registration_status\": \"Registered\",\n" +
            "    \"stolen_status\": \"No report\",\n" +
            "    \"pairs\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"last_seen\": \"9999-99-99\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"imei\": \"12345678912345\",\n" +
            "    \"classification_state\": {\n" +
            "        \"blocking_conditions\": [\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"gsma_not_found\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"on_local_stolen_list\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"not_on_registration_list\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"informative_conditions\": [\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"duplicate_compound\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"gsma\": {\n" +
            "        \"brand\": \"Not Known\",\n" +
            "        \"manufacturer\": \"BlackBerry Limited\",\n" +
            "        \"model_name\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"device_type\": \"Handheld\",\n" +
            "        \"radio_access_technology\": \"GSM 1800,GSM 900\",\n" +
            "        \"model_number\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"operating_system\": null\n" +
            "    },\n" +
            "    \"subscribers\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-12-31\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"compliant\": {\n" +
            "        \"status\": \"Compliant (Active)\"\n" +
            "    }\n" +
            "}";

    public static final String loadMorePairedNoData = "{\n" +
            "    \"registration_status\": \"Registered\",\n" +
            "    \"stolen_status\": \"No report\",\n" +
            "    \"pairs\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"imei\": \"12345678912345\",\n" +
            "    \"classification_state\": {\n" +
            "        \"blocking_conditions\": [\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"gsma_not_found\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"on_local_stolen_list\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"not_on_registration_list\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"informative_conditions\": [\n" +
            "            {\n" +
            "                \"condition_met\": false,\n" +
            "                \"condition_name\": \"duplicate_compound\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"gsma\": {\n" +
            "        \"brand\": \"Not Known\",\n" +
            "        \"manufacturer\": \"BlackBerry Limited\",\n" +
            "        \"model_name\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"device_type\": \"Handheld\",\n" +
            "        \"radio_access_technology\": \"GSM 1800,GSM 900\",\n" +
            "        \"model_number\": \"This is a Test IMEI to be used with multiple prototype models. The frequency bands for each model may not match what is listed in this record\",\n" +
            "        \"operating_system\": null\n" +
            "    },\n" +
            "    \"subscribers\": {\n" +
            "        \"count\": 3,\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-11-30\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "\t\t\t{\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-10-31\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"imsi\": \"111010000000597\",\n" +
            "                \"msisdn\": \"223000000000605\",\n" +
            "                \"last_seen\": \"2017-12-31\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"limit\": 10,\n" +
            "        \"start\": 1\n" +
            "    },\n" +
            "    \"compliant\": {\n" +
            "        \"status\": \"Compliant (Active)\"\n" +
            "    }\n" +
            "}";

}
