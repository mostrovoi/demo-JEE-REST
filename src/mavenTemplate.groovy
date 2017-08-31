#!/usr/bin/groovy
def call() {
podTemplate(label: label,
        containers: [containerTemplate(name: 'maven', image: 'maven', command: 'cat', ttyEnabled: true)],
        volumes: [secretVolume(secretName: 'maven-settings', mountPath: '/root/.m2'),
                  persistentVolumeClaim(claimName: 'maven-local-repo', mountPath: '/root/.m2nrepo')]) {
    body()
}