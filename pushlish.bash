#!/bin/bash

sbt \
  clean \
  core/publishSigned \
  clean \
  jdbc/publishSigned \
  clean \
  play/publishSigned
