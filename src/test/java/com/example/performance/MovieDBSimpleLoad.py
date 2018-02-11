__author__ = 'Marvin Dean'

from locust import HttpLocust, TaskSet, task
from random import randint
import string

class NodeEndpoints(TaskSet):

    @task(100)
    def getQuoteByIndex(self):
        movieid = (randint(1, 900))
        self.client.get("/movie/" + str(movieid), name="/movie/:id GET")

class WebsiteUser(HttpLocust):
    task_set = NodeEndpoints
    min_wait = 5000
    max_wait = 10000