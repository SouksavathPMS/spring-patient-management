#!/bin/zsh

set -e

echo "Cleaning up existing resources..."

# Delete existing stack
aws --endpoint-url=http://localhost:4566 cloudformation delete-stack \
    --stack-name patient-management 2>/dev/null || true

# Delete log groups
for log_group in /ecs/api-gateway /ecs/auth-service /ecs/billing-service /ecs/analytics-service /ecs/patient-service; do
    aws --endpoint-url=http://localhost:4566 logs delete-log-group \
        --log-group-name $log_group 2>/dev/null || true
done

echo "Waiting for cleanup to complete..."
sleep 10

echo "Deploying stack..."
aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name patient-management \
    --template-file "./cdk.out/localstack.template.json"

aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text