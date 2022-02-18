package com.ataraxia.artemis.ui

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ataraxia.artemis.model.Screen.DrawerScreen.*
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class QuestionCatalogueComponent {

    @Composable
    fun QuestionScreen(navController: NavHostController, scope: CoroutineScope) {
        val localConfiguration = LocalConfiguration.current
        when (localConfiguration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(15.dp)
                            .size(400.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { navController.navigate(TopicWildLife.route) },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Wildbiologie & Wildhege",
                                color = Color.Black,
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(15.dp)
                            .size(400.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { navController.navigate(TopicHuntingOperations.route) },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Jagdbetrieb",
                                color = Color.Black,
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(15.dp)
                            .size(400.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { navController.navigate(TopicWeaponsLawAndTechnology.route) },
                            modifier = Modifier.background(YELLOW_ARTEMIS),
                        ) {
                            Text(
                                text = "Waffenrecht & Technik",
                                color = Color.Black,
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(15.dp)
                            .size(400.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { navController.navigate(TopicWildLifeTreatment.route) },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Behandlung des Wildes",
                                color = Color.Black,
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(15.dp)
                            .size(400.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { navController.navigate(TopicHuntingLaw.route) },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Jagdrecht",
                                color = Color.Black,
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(15.dp)
                            .size(400.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { navController.navigate(TopicPreservationOfWildLifeAndNature.route) },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Tier- und Naturschutz",
                                color = Color.Black,
                            )
                        }
                    }
                }
            }
            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(250.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Wildbiologie & Wildhege",
                                color = Color.Black,
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(250.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { scope.launch { navController.navigate("topics") } },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Jagdbetrieb",
                                color = Color.Black,
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(250.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.background(YELLOW_ARTEMIS),
                        ) {
                            Text(
                                text = "Waffenrecht & Technik",
                                color = Color.Black,
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(250.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Behandlung des Wildes",
                                color = Color.Black,
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(250.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Jagdrecht",
                                color = Color.Black,
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(250.dp, 35.dp)
                            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    ) {
                        TextButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.background(YELLOW_ARTEMIS)
                        ) {
                            Text(
                                text = "Tier- und Naturschutz",
                                color = Color.Black,
                            )
                        }
                    }
                }
            }
        }
    }
}


